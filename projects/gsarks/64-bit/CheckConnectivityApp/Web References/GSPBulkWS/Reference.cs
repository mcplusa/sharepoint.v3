﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:2.0.50727.3625
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// 
// This source code was auto-generated by Microsoft.VSDesigner, Version 2.0.50727.3625.
// 
#pragma warning disable 1591

namespace WindowsApplication1.GSPBulkWS {
    using System.Diagnostics;
    using System.Web.Services;
    using System.ComponentModel;
    using System.Web.Services.Protocols;
    using System;
    using System.Xml.Serialization;
    
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "2.0.50727.3053")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Web.Services.WebServiceBindingAttribute(Name="BulkAuthorizationSoap", Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public partial class BulkAuthorization : System.Web.Services.Protocols.SoapHttpClientProtocol {
        
        private System.Threading.SendOrPostCallback CheckConnectivityOperationCompleted;
        
        private System.Threading.SendOrPostCallback AuthorizeOperationCompleted;
        
        private bool useDefaultCredentialsSetExplicitly;
        
        /// <remarks/>
        public BulkAuthorization() {
            this.Url = global::WindowsApplication1.Properties.Settings.Default.Verify_Installation_GSPBulkWS_BulkAuthorization;
            if ((this.IsLocalFileSystemWebService(this.Url) == true)) {
                this.UseDefaultCredentials = true;
                this.useDefaultCredentialsSetExplicitly = false;
            }
            else {
                this.useDefaultCredentialsSetExplicitly = true;
            }
        }
        
        public new string Url {
            get {
                return base.Url;
            }
            set {
                if ((((this.IsLocalFileSystemWebService(base.Url) == true) 
                            && (this.useDefaultCredentialsSetExplicitly == false)) 
                            && (this.IsLocalFileSystemWebService(value) == false))) {
                    base.UseDefaultCredentials = false;
                }
                base.Url = value;
            }
        }
        
        public new bool UseDefaultCredentials {
            get {
                return base.UseDefaultCredentials;
            }
            set {
                base.UseDefaultCredentials = value;
                this.useDefaultCredentialsSetExplicitly = true;
            }
        }
        
        /// <remarks/>
        public event CheckConnectivityCompletedEventHandler CheckConnectivityCompleted;
        
        /// <remarks/>
        public event AuthorizeCompletedEventHandler AuthorizeCompleted;
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com/CheckCon" +
            "nectivity", RequestNamespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com", ResponseNamespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public string CheckConnectivity() {
            object[] results = this.Invoke("CheckConnectivity", new object[0]);
            return ((string)(results[0]));
        }
        
        /// <remarks/>
        public void CheckConnectivityAsync() {
            this.CheckConnectivityAsync(null);
        }
        
        /// <remarks/>
        public void CheckConnectivityAsync(object userState) {
            if ((this.CheckConnectivityOperationCompleted == null)) {
                this.CheckConnectivityOperationCompleted = new System.Threading.SendOrPostCallback(this.OnCheckConnectivityOperationCompleted);
            }
            this.InvokeAsync("CheckConnectivity", new object[0], this.CheckConnectivityOperationCompleted, userState);
        }
        
        private void OnCheckConnectivityOperationCompleted(object arg) {
            if ((this.CheckConnectivityCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.CheckConnectivityCompleted(this, new CheckConnectivityCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com/Authoriz" +
            "e", RequestNamespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com", ResponseNamespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public void Authorize(ref AuthDataPacket[] authDataPacketArray, string username) {
            object[] results = this.Invoke("Authorize", new object[] {
                        authDataPacketArray,
                        username});
            authDataPacketArray = ((AuthDataPacket[])(results[0]));
        }
        
        /// <remarks/>
        public void AuthorizeAsync(AuthDataPacket[] authDataPacketArray, string username) {
            this.AuthorizeAsync(authDataPacketArray, username, null);
        }
        
        /// <remarks/>
        public void AuthorizeAsync(AuthDataPacket[] authDataPacketArray, string username, object userState) {
            if ((this.AuthorizeOperationCompleted == null)) {
                this.AuthorizeOperationCompleted = new System.Threading.SendOrPostCallback(this.OnAuthorizeOperationCompleted);
            }
            this.InvokeAsync("Authorize", new object[] {
                        authDataPacketArray,
                        username}, this.AuthorizeOperationCompleted, userState);
        }
        
        private void OnAuthorizeOperationCompleted(object arg) {
            if ((this.AuthorizeCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.AuthorizeCompleted(this, new AuthorizeCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        public new void CancelAsync(object userState) {
            base.CancelAsync(userState);
        }
        
        private bool IsLocalFileSystemWebService(string url) {
            if (((url == null) 
                        || (url == string.Empty))) {
                return false;
            }
            System.Uri wsUri = new System.Uri(url);
            if (((wsUri.Port >= 1024) 
                        && (string.Compare(wsUri.Host, "localHost", System.StringComparison.OrdinalIgnoreCase) == 0))) {
                return true;
            }
            return false;
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "2.0.50727.3082")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public partial class AuthDataPacket {
        
        private AuthData[] authDataArrayField;
        
        private Container containerField;
        
        private string messageField;
        
        private bool isDoneField;
        
        /// <remarks/>
        public AuthData[] AuthDataArray {
            get {
                return this.authDataArrayField;
            }
            set {
                this.authDataArrayField = value;
            }
        }
        
        /// <remarks/>
        public Container Container {
            get {
                return this.containerField;
            }
            set {
                this.containerField = value;
            }
        }
        
        /// <remarks/>
        public string Message {
            get {
                return this.messageField;
            }
            set {
                this.messageField = value;
            }
        }
        
        /// <remarks/>
        public bool IsDone {
            get {
                return this.isDoneField;
            }
            set {
                this.isDoneField = value;
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "2.0.50727.3082")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public partial class AuthData {
        
        private Container containerField;
        
        private string itemIdField;
        
        private bool isAllowedField;
        
        private string messageField;
        
        private string complexDocIdField;
        
        private bool isDoneField;
        
        private EntityType typeField;
        
        /// <remarks/>
        public Container Container {
            get {
                return this.containerField;
            }
            set {
                this.containerField = value;
            }
        }
        
        /// <remarks/>
        public string ItemId {
            get {
                return this.itemIdField;
            }
            set {
                this.itemIdField = value;
            }
        }
        
        /// <remarks/>
        public bool IsAllowed {
            get {
                return this.isAllowedField;
            }
            set {
                this.isAllowedField = value;
            }
        }
        
        /// <remarks/>
        public string Message {
            get {
                return this.messageField;
            }
            set {
                this.messageField = value;
            }
        }
        
        /// <remarks/>
        public string ComplexDocId {
            get {
                return this.complexDocIdField;
            }
            set {
                this.complexDocIdField = value;
            }
        }
        
        /// <remarks/>
        public bool IsDone {
            get {
                return this.isDoneField;
            }
            set {
                this.isDoneField = value;
            }
        }
        
        /// <remarks/>
        public EntityType Type {
            get {
                return this.typeField;
            }
            set {
                this.typeField = value;
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "2.0.50727.3082")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public partial class Container {
        
        private string urlField;
        
        private ContainerType typeField;
        
        /// <remarks/>
        public string Url {
            get {
                return this.urlField;
            }
            set {
                this.urlField = value;
            }
        }
        
        /// <remarks/>
        public ContainerType Type {
            get {
                return this.typeField;
            }
            set {
                this.typeField = value;
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "2.0.50727.3082")]
    [System.SerializableAttribute()]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public enum ContainerType {
        
        /// <remarks/>
        NA,
        
        /// <remarks/>
        SITE_COLLECTION,
        
        /// <remarks/>
        SITE,
        
        /// <remarks/>
        LIST,
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "2.0.50727.3082")]
    [System.SerializableAttribute()]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="gsbulkauthorization.generated.sharepoint.connector.enterprise.google.com")]
    public enum EntityType {
        
        /// <remarks/>
        LISTITEM,
        
        /// <remarks/>
        LIST,
        
        /// <remarks/>
        ALERT,
        
        /// <remarks/>
        SITE,
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "2.0.50727.3053")]
    public delegate void CheckConnectivityCompletedEventHandler(object sender, CheckConnectivityCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "2.0.50727.3053")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class CheckConnectivityCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal CheckConnectivityCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public string Result {
            get {
                this.RaiseExceptionIfNecessary();
                return ((string)(this.results[0]));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "2.0.50727.3053")]
    public delegate void AuthorizeCompletedEventHandler(object sender, AuthorizeCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "2.0.50727.3053")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class AuthorizeCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal AuthorizeCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public AuthDataPacket[] authDataPacketArray {
            get {
                this.RaiseExceptionIfNecessary();
                return ((AuthDataPacket[])(this.results[0]));
            }
        }
    }
}

#pragma warning restore 1591