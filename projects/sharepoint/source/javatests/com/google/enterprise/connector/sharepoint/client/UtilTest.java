// Copyright 2007 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.sharepoint.client;

import static com.google.enterprise.connector.spi.SpiConstants.CaseSensitivityType.EVERYTHING_CASE_INSENSITIVE;
import static com.google.enterprise.connector.spi.SpiConstants.PrincipalType.UNQUALIFIED;

import com.google.enterprise.connector.sharepoint.TestConfiguration;
import com.google.enterprise.connector.sharepoint.client.SPConstants.FeedType;
import com.google.enterprise.connector.spi.Principal;
import com.google.enterprise.connector.spi.RepositoryException;

import org.joda.time.DateTime;

import java.util.List;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class UtilTest extends TestCase {

  protected void setUp() throws Exception {
    System.out.println("\n--------------");
  }

  public final void testSiteDataStringToCalendar() {
    System.out.println("Testing siteDataStringToCalendar()...");
    final String listItemChangesString = "2008-07-16 5:30:35Z"; // Date in
    // UTC
    // format
    Calendar cal = null;
    try {
      cal = Util.siteDataStringToCalendar(listItemChangesString); // UTC
      // format
      // to
      // Calender
      System.out.println("Method Returned : " + cal);
      assertNotNull(cal);
      final Date dt = cal.getTime();
      System.out.println("Date : " + dt);
    } catch (final ParseException pe) {
      System.out.println(pe);
      System.out.println("[ siteDataStringToCalendar() ] Test Failed.");
      return;
    }
    System.out.println("[ siteDataStringToCalendar() ] Test Passed");
  }

  public final void testJodaToCalendar() {
    System.out.println("Testing jodaToCalendar()...");
    Calendar cal = null;
    cal = Util.jodaToCalendar(new DateTime());
    System.out.println("Method Returned : " + cal);
    assertNotNull(cal);
    final Date dt = cal.getTime();
    System.out.println("Date : " + dt);
    System.out.println("[ jodaToCalendar() ] Test Passed");
  }

  public final void testCalendarToJoda() {
    System.out.println("Testing calendarToJoda()...");
    final DateTime dt = Util.calendarToJoda(Calendar.getInstance());
    System.out.println("Method Returned : " + dt);
    assertNotNull(dt);
    System.out.println("[ calendarToJoda() ] Test Passed");
  }

  public final void testFormatDateDateTime() {
    System.out.println("Testing formatDate(DateTime)...");
    final String dt = Util.formatDate(new DateTime());
    System.out.println("Method Returned : " + dt);
    assertNotNull(dt);
    System.out.println("[ formatDate() ] Test Passed");
  }

  public final void testFormatDateCalendar() {
    System.out.println("Testing formatDate(Calendar)...");
    final String dt = Util.formatDate(Calendar.getInstance());
    System.out.println("Method Returned : " + dt);
    assertNotNull(dt);
    System.out.println("[ formatDate() ] Test Passed");
  }

  public final void testParseDate() {
    System.out.println("Testing parseDate(Calendar)...");
    final DateTime dt = Util.parseDate("20090116T205013.000+0530");
    System.out.println("Method Returned : " + dt);
    assertNotNull(dt);
    System.out.println("[ parseDate() ] Test Passed");
  }

  public final void testMatcher() {
    System.out.println("Testing matcher()..");
    final boolean bl = Util.match(new String[] { "sp.intranet.teldta.com/" }, "https://sp.intranet.teldta.com", null);
    System.out.println(bl);
    System.out.println("[ matcher() ] Test Completed");
  }

  public void testGetCurrentTimestampString() {
    String formattedDate = Util.getCurrentTimestampString();
    assertTrue(formattedDate, formattedDate.matches(
        "201[4-9]-[01]\\d-[0-3]\\d [0-2]\\d:[0-6]\\d:[0-6]\\d .+"));
  }

  public void testDoAliasMapping() {
    checkAliasMapping("http://mycomp.com/", "http://mycomp.com:80/");
    checkAliasMapping("http://mycomp.com:80/", "http://mycomp.com/");
    checkAliasMapping("http://mycomp.com:80/", "http://mycomp.com:8080/");
    checkAliasMapping("http://mycomp.com:80/", "http://mycomp.co.in:8080/");
    checkAliasMapping("http://mycomp.com:80/", "https://mycomp.co.in:8080/");
  }

  private void checkAliasMapping(final String originalUrl,
      final String expectedUrl) {
    Map<String, String> aliasMap = new HashMap<String, String>();
    aliasMap.put(originalUrl, expectedUrl);
    String mappedUrl = null;
    try {
      mappedUrl = Util.doAliasMapping(originalUrl, aliasMap, false);
    } catch (Exception e) {
      fail(e.getMessage());
    }
    assertEquals(expectedUrl, mappedUrl);
    aliasMap.clear();
  }

  /**
   * @Test Tests {@link Util#getGroupNameWithDomain(groupname, domain)}
   */
  public final void testGetGroupNameWithDomain() {
    String expectedGroupNameFormat = "testdomain\\testgroup";
    String format1 = Util.getGroupNameWithDomain("testgroup", "testdomain");
    assertNotNull(format1);
    assertEquals(expectedGroupNameFormat, format1);
    String format2 = Util.getGroupNameWithDomain("testgroup@testdomain", "testdomain");
    assertNotNull(format2);
    assertEquals(expectedGroupNameFormat, format2);
    String format3 = Util.getGroupNameWithDomain("testdomain\\testgroup", "testdomain");
    assertNotNull(format3);
    assertEquals(expectedGroupNameFormat, format3);
  }

  /**
   * @Test Tests {@link Util#getGroupNameAtDomain(groupname, domain)}
   */
  public final void testGetGroupNameAtDomain() {
    String expectedGroupNameFormat = "testgroup@testdomain";
    String format1 = Util.getGroupNameAtDomain("testgroup", "testdomain");
    assertNotNull(format1);
    assertEquals(expectedGroupNameFormat, format1);
    String format2 = Util.getGroupNameAtDomain("testgroup@testdomain", "testdomain");
    assertNotNull(format2);
    assertEquals(expectedGroupNameFormat, format2);
    String format3 = Util.getGroupNameAtDomain("testdomain\\testgroup", "testdomain");
    assertNotNull(format3);
    assertEquals(expectedGroupNameFormat, format3);
  }

  public void testGetSharePointGroupPrincipal() {
    assertEquals(new Principal(UNQUALIFIED, "ns", "[url]name",
            EVERYTHING_CASE_INSENSITIVE),
        Util.getSharePointGroupPrincipal("ns", "url", "name"));
  }

  /**
   * @Test Tests {@link Util#processMultiValueMetadata(String)}
   * for multi choice fields
   */
  public final void testProcessMultiValueMetadata_multiChoice() {
    // Multichoice field format
    String multiChoiceValue = ";#Value1;#Value2;#Value3;#";
    List<String> choiceValues =  Util.processMultiValueMetadata(multiChoiceValue);
    assertEquals(3, choiceValues.size());
    assertEquals("Value1", choiceValues.get(0));
    assertEquals("Value2", choiceValues.get(1));
    assertEquals("Value3", choiceValues.get(2));
  }

  /**
   * @Test Tests {@link Util#processMultiValueMetadata(String)}
   * for multi lookup fields
   */
  public final void testProcessMultiValueMetadata_multiLookup() {
    // MultiLookup field format
    String multiLookupValue = "1;#Value1;#2;#Value2;#3;#Value3;#4;#Value4";
    List<String> lookupValues =
        Util.processMultiValueMetadata(multiLookupValue);
    assertEquals(4, lookupValues.size());
    assertEquals("Value1", lookupValues.get(0));
    assertEquals("Value2", lookupValues.get(1));
    assertEquals("Value3", lookupValues.get(2));
    assertEquals("Value4", lookupValues.get(3));
  }

  /**
   * @Test Tests {@link Util#processMultiValueMetadata(String)}
   * for sharepoint internal fields and single value fields
   */
  public final void testProcessMultiValueMetadata_others() {
    // SharePoint InternalField Format
    String internalValue = "1;#Value1";
    List<String> internalValues =
        Util.processMultiValueMetadata(internalValue);
    assertEquals(1, internalValues.size());
    assertEquals("Value1", internalValues.get(0));

    // Normal text with ;# part of it but not multi-value - should not process
    String normalValue = "normal;#value;#somemore";
    List<String> normalValues =  Util.processMultiValueMetadata(normalValue);
    assertEquals(1, normalValues.size());
    assertEquals(normalValue, normalValues.get(0));
  }

  /**
   * @Test Tests {@link Util#normalizeMetadataValue(String)}
   */
  public final void testNormalizeMetadataValue() {
    // SharePoint Internal field - should process
    String internalValue = "1;#Value1";
    assertEquals("Value1", Util.normalizeMetadataValue(internalValue));

    // Multi-choice field - should not process
    String multiChoiceValue = ";#Value1;#Value2;#Value3;#";
    assertEquals(multiChoiceValue,
        Util.normalizeMetadataValue(multiChoiceValue));

    // Multi-value lookup field - should not process
    String multiLookupValue = "1;#Value1;#2;#Value2;#3;#Value3;#4;#Value4";
    assertEquals(multiLookupValue,
        Util.normalizeMetadataValue(multiLookupValue));
  }
}
