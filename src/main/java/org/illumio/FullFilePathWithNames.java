package org.illumio;

import java.nio.file.Path;

public class FullFilePathWithNames {

    public static String protocolMappingFileName =
            Path.of("src", "main", "resources", "input", "protocol_mapping.csv").toString();
    public static String lookupTableFileName =
            Path.of("src", "main", "resources", "input", "lookup_table.csv").toString();
    public static String flowLogRecordFileName =
            Path.of("src", "main", "resources", "input", "flow_log_record.log").toString();
    public static String tagCountFileName =
            Path.of("src", "main", "resources", "output", "tag_count.csv").toString();
    public static String portProtocolCountFileName =
            Path.of("src", "main", "resources", "output", "port_protocol_count.csv").toString();
}
