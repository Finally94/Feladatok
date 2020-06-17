module degubi.admintools {
    opens degubi to org.eclipse.yasson;
    
    requires java.desktop;
    requires java.net.http;
    
    requires java.json;
    requires java.json.bind;
    requires org.eclipse.yasson;
}