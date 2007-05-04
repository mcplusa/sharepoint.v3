//Copyright (C) 2006 Google Inc.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.google.enterprise.connector.sharepoint.state;

import com.google.enterprise.connector.persist.PrefsStore;
import com.google.enterprise.connector.sharepoint.Util;
import com.google.enterprise.connector.sharepoint.client.SharepointException;
import com.google.enterprise.connector.sharepoint.state.ListState;
import com.google.enterprise.connector.sharepoint.state.StatefulObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Represents the state of a site. Can be persisted to XML and
 * loaded again from the XML. This would normally be done if the
 * connector wants to be restartable.
 * The state of SharePoint traversal is composed almost entirely of the 
 * state of other classes, which must implement the StatefulObject interface. 
 * As of May 2007, there is only one StatefulObject -- ListState.
 *
 * Classes:
 *    GlobalState.
 * related classes:
 *   StatefulObject (interface)
 *   ListState (implements StatefulObject)
 *
 */
public class GlobalState {
  private static Log logger = LogFactory.getLog(GlobalState.class);
  private static final String CONNECTOR_NAME = "Sharepoint";

  private boolean refreshing = false;

  /**
   * To keep track of ListStates, we keep two data structures: a TreeSet relying
   * on the lastMod property of a StatefulObject, and a HashMap on the
   * primary key of the object (guid for Lists).  The StatefulObject interface
   * is often used to reduce our dependency on peculiarities of ListState. (At
   * one time, it was thought that there would be other instances of 
   * StatefulObject, and in the future there could be again)
   */
  protected TreeSet<ListState> dateMap = new TreeSet<ListState>();
  protected HashMap<String, ListState> keyMap = 
    new HashMap<String, ListState>();

  /**
   * The "current" object for ListState. The current object may be null.
   */
  protected ListState currentObj = null;

  /**
   * Constructor. 
   */
  public GlobalState() {
  }

  /**
   * Factory method for ListState.
   * @param key the "primary key" of the object. This would
   * probably be the GUID.
   * @param lastMod most recent time this object was modified.
   * @return new object
   */
  public ListState makeListState(String key, DateTime lastMod) {
    ListState obj = new ListState(); 
    obj.setLastMod(lastMod);
    obj.setPrimaryKey(key);
    updateList(obj, lastMod); // add to our maps
    return obj;
  }

  /**
   * Convenience factory for clients who don't deal in Joda time.
   * @param key
   * @param lastModCal (Calendar, not Joda time)
   * @return new ListState
   */
  public ListState makeListState(String key, Calendar lastModCal) {
    return makeListState(key, Util.calendarToJoda(lastModCal));
  }

  /**
   * Signal that a complete "refresh" cycle is beginning, where all lists
   * are being fetched from SharePoint.  This GlobalState will
   * keep track of which objects are still present.  At the endRefresh()
   * call, objects no longer existing may be removed.
   */
  public void startRefresh() {
    refreshing = true;
    
    // for each ListState, set "not existing"
    for (Iterator<ListState> iter = dateMap.iterator(); iter.hasNext(); ) {
      ListState obj = iter.next();
      obj.setExisting(false);
    }
  }

  /**
   * Signals that the refresh cycle is over, and GlobalState may now
   * delete any ListState which did not appear in a 
   * updateList() call since the startRefresh() call.
   */
  public void endRefresh() {
    if (!refreshing) {
      logger.error("called endRefresh() when not in a refresh state");
      return;
    }
    for (Iterator<ListState> iter = getIterator(); iter.hasNext(); ) {
      ListState obj = iter.next(); 
      if (!obj.isExisting()) {
        iter.remove(); // we MUST use the iterator's own remove() here
        keyMap.remove(obj.getPrimaryKey());
      }
    }
    refreshing = false;
  }

  /**
   * Get an iterator which returns the objects in increasing order of their
   * lastModified dates
   * @return Iterator on the objects by lastModified time
   */
  public Iterator<ListState> getIterator() {
    return dateMap.iterator();
  }

  /**
   * Lookup a ListState by its key
   * @param key primary key
   * @return object handle, or null if none found
   */
  public ListState lookupList(String key) {
    return keyMap.get(key);
  }

  /**
   * Return an XML string representing the current state. Since our state
   * is comprised almost entirely of the state of the StatefulObjects
   * (i.e. ListState), this is largely done by calling an
   * analogous method on those classes. Here is an example (note that
   * the only state that belongs to GlobalState itself if the "current"
   * object for each dependent class).  Consult ListState for details on
   * its XML representation.
   * <pre>
    <?xml version="1.0" encoding="UTF-8"?>
    <state>
      <current id="foo" type="ListState"/>
      <ListState id="bar">
        <lastMod>20070420T154348.133-0700</lastMod>
        <URL/>
        <lastDocCrawled>
          <document id="id2">
            <lastMod>20070420T154348.133-0700</lastMod>
            <url>url2</url>
          </document>
        </lastDocCrawled>
      </ListState>
      <ListState id="foo">
        <lastMod>20070420T154348.133-0700</lastMod>
        <URL/>
        <lastDocCrawled>
          <document id="id1">
            <lastMod>20070420T154348.133-0700</lastMod>
            <url>url1</url>
          </document>
        </lastDocCrawled>
        <crawlQueue>
          <document id="id3">
            <lastMod>20070420T154348.133-0700</lastMod>
            <url>url3</url>
          </document>
          <document id="id4">
            <lastMod>20070420T154348.133-0700</lastMod>
            <url>url4</url>
          </document>
        </crawlQueue>
      </ListState>
    </state>
   * </pre>
   * @return XML string
   */
  public String getStateXML() throws SharepointException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    org.w3c.dom.Document doc;
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.newDocument();
    } catch (ParserConfigurationException e) {
      throw new SharepointException(e.toString());
    }
    Element top = doc.createElement("state");
    doc.appendChild(top);

    if (currentObj != null) {
      Element element = doc.createElement("current");
      element.setAttribute("type", ListState.class.getSimpleName());
      element.setAttribute("id", currentObj.getPrimaryKey());
      top.appendChild(element);
    }

    // now dump the actual ListStates:
    for (Iterator<ListState> iterObj = dateMap.iterator();
        iterObj.hasNext(); ) {
      StatefulObject obj = iterObj.next();
      top.appendChild(obj.dumpToDOM(doc));
    }

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer t = null;
    try {
      t = tf.newTransformer();
    } catch (TransformerConfigurationException e) {
      throw new SharepointException(e.toString());
    }
    t.setOutputProperty(OutputKeys.INDENT, "yes");
    t.setOutputProperty(OutputKeys.METHOD, "xml");
    t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    DOMSource doms = new DOMSource(doc);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    StreamResult sr = new StreamResult(os);
    try {
      t.transform(doms, sr);
    } catch (TransformerException e) {
      throw new SharepointException(e.toString());
    }
    return os.toString();
  }

  /**
   * Creates an XML representation of our state, and saves it, using the
   * PrefsStore mechanism.
   * @throws SharepointException
   */
  public void saveState()  throws SharepointException {
    PrefsStore store = new PrefsStore();
    String xml = getStateXML();
    logger.info(xml);
    store.storeConnectorState(CONNECTOR_NAME, xml);
  }

  /**
   * Load from XML.
   * @param persisted - a string representing the output of a previous
   * getStateXML() call.
   */
  public void loadStateXML(String persisted) throws SharepointException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      StringReader stringReader = new StringReader(persisted);
      org.w3c.dom.Document doc = builder.parse(new InputSource(stringReader));
      NodeList nodeList = doc.getElementsByTagName("state");
      if (nodeList.getLength() == 0) {
        throw new SharepointException("Invalid XML: no <state> element");
      }
      // temporary list of the "current" objects (just their keys):
      String currentKeyTmp = null;

      NodeList children = nodeList.item(0).getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node node = children.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        Element element = (Element) children.item(i);
        if (element.getTagName().equals("current")) {
          String type = element.getAttribute("type");
          String id = element.getAttribute("id");
          if (type != null && id != null) {
            currentKeyTmp = id;
          }
          continue;
        }
        if (!element.getTagName().equals(ListState.class.getSimpleName())) {
          continue; //no exception; ignore xml for things we don't understand
        }
        ListState subObject = new ListState(); 
        subObject.loadFromDOM(element);
        updateList(subObject, subObject.getLastMod());

        // now, for "current", for which so far we've only the key, find the
        // actual object:
        if (currentKeyTmp != null) {
          currentObj = keyMap.get(currentKeyTmp);
        }
      }
    } catch (IOException e) {
      logger.error(e);
      throw new SharepointException(e.toString());
    } catch (ParserConfigurationException e) {
      logger.error(e);
      throw new SharepointException(e.toString());
    } catch (SAXException e) {
      logger.error(e);
      throw new SharepointException(e.toString());
    }
  }

  /**
   * Load persistent state from our XML state
   * @throws SharepointException if the XML file can't be found, or is
   * invalid in any way.
   */
  public void loadState() throws SharepointException {
    PrefsStore store = new PrefsStore();
    String state = store.getConnectorState(CONNECTOR_NAME);
    if (state != null) {
      loadStateXML(state);
    }
  }

  /**
   * Set the given List as "current"
   * This will be remembered in the XML state.
   * @param obj
   */
  public void setCurrentList(ListState obj) {
    currentObj = obj;
  }

  /**
   * Get the current ListState
   * @return the current object, or null if none
   */
  public ListState getCurrentList() {
    return currentObj;
  }

  /**
   * For a single StatefulObject, update the two
   * data structures (url -> obj and time -> obj) and mark
   * it "Existing" (if between startRefresh() and endRefresh())
   * @param ListState
   * @param time
   */
  public void updateList(ListState state, DateTime time) {
    ListState stateOld = keyMap.get(state.getPrimaryKey());
    if (stateOld != null) {
      if (stateOld.getLastMod().compareTo(time) != 0) { // if new time differs
        dateMap.remove(state);
        state.setLastMod(time);
      }
    } else {
      state.setLastMod(time);
      keyMap.put(state.getPrimaryKey(), state);
    }
    if (refreshing) {
      state.setExisting(true); // remember we saw this one!
    }
    dateMap.add(state);
  }

  /**
   * Mark all the dependent objects "Existing"
   * @param simpleName
   * @param Existing
   */
  private void setAllExisting(boolean existing) {
    Set<Entry<String, ListState>> entries = keyMap.entrySet();
    for (Iterator iter = entries.iterator(); iter.hasNext(); ) {
      Map.Entry<String, ListState> entry =
        (Entry<String, ListState>) iter.next();
      ListState state = entry.getValue();
      state.setExisting(existing);
    }
  }

}
