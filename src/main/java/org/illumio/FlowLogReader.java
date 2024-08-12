package org.illumio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.illumio.FullFilePathWithNames.*;

public class FlowLogReader {

    // For mapping input files to Java objects
    private final HashMap<String, String> protocolMap;
    private final HashMap<Map.Entry<String, String>, String> lookupTable;

    // Output Java objects to be written in output result files
    private final HashMap<String, Integer> tagCount;
    private final HashMap<Map.Entry<String, String>, Integer> portProtocolCount;
    public FlowLogReader() throws IOException {
        this.protocolMap = new HashMap<>();
        this.lookupTable = new HashMap<>();
        this.tagCount = new HashMap<>();
        this.portProtocolCount = new HashMap<>();

        // Load protocol mapping and lookup table to memory
        loadProtocolNames();
        loadLookupTable();
    }
    public void parseFlowLogFileAndPopulateResultFiles() throws IOException {

        // Step 1: Read flowLogRecord file
        try (RandomAccessFile raf = new RandomAccessFile(flowLogRecordFileName, "r")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String[] lineContent = line.split(" ");
                // If the log flow record is of version 2, populate the dstport and protocol
                if (lineContent.length >= 8) {
                    String dstport = lineContent[6];
                    String protocolName = protocolMap.get(lineContent[7]);
                    if("-".equals(dstport) || "-".equals(protocolName)) {
                        continue;
                    }
                    Map.Entry<String, String> portProtocolPair = new AbstractMap.SimpleEntry<>(dstport, protocolName);

                    // Populate tagCount map
                    String tag = "Untagged";
                    if(lookupTable.containsKey(portProtocolPair)) {
                        tag = lookupTable.get(portProtocolPair);
                    }
                    int count = 1;
                    if(tagCount.containsKey(tag)) {
                        count = tagCount.get(tag) + 1;
                    }
                    tagCount.put(tag, count);

                    // Populate portProtocolCount map
                    count = 1;
                    if(portProtocolCount.containsKey(portProtocolPair)) {
                        count = portProtocolCount.get(portProtocolPair) + 1;
                    }
                    portProtocolCount.put(portProtocolPair, count);
                }
            }
        }

        // Step 2a: Write tagCount file
        try (RandomAccessFile raf = new RandomAccessFile(tagCountFileName, "rw")) {
            raf.write("Tag,Count".getBytes());
            for(String tag: tagCount.keySet()) {
                raf.write(String.format("%n%s,%s",
                        tag,
                        tagCount.get(tag))
                    .getBytes());
            }
        }

        // Step 2b: Write portProtocolCount file
        try (RandomAccessFile raf = new RandomAccessFile(portProtocolCountFileName, "rw")) {
            raf.write("Port,Protocol,Count".getBytes());
            for(Map.Entry<String, String> portProtocolPair: portProtocolCount.keySet()) {
                raf.write(String.format("%n%s,%s,%s",
                        portProtocolPair.getKey(),
                        portProtocolPair.getValue(),
                        portProtocolCount.get(portProtocolPair))
                    .getBytes());
            }
        }
    }

    public void loadProtocolNames() throws IOException {
        boolean firstLine = true;
        try (RandomAccessFile raf = new RandomAccessFile(protocolMappingFileName, "r")) {
            String line;
            while((line = raf.readLine()) != null) {
                // Ignore the first line since it is the header
                if(firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineContent = line.replaceAll(" ", "").split(",");
                if(lineContent.length == 2) {
                    // protocol id and name are both present
                    protocolMap.put(lineContent[0], lineContent[1].toLowerCase());
                } else if(lineContent.length == 1) {
                    // protocol name is empty
                    protocolMap.put(lineContent[0], "");
                } else {
                    // Hit an empty line in the protocol_mapping.csv file
                }
            }
        }
    }

    public void loadLookupTable() throws IOException {
        boolean firstLine = true;
        try (RandomAccessFile raf = new RandomAccessFile(lookupTableFileName, "r")) {
            String line;
            while((line = raf.readLine()) != null) {
                // Ignore the first line since it is the header
                if(firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineContent = line.replaceAll(" ", "").split(",");
                if(lineContent.length == 3) {
                    // dstport, protocol & tag - all 3 entries are present
                    lookupTable.put(new AbstractMap.SimpleEntry<>(lineContent[0], lineContent[1].toLowerCase()), lineContent[2]);
                } else {
                    // One of the columns is empty or there's some error in the lookup_table.csv
                    // Skip adding erroneous lookup entries
                }
            }
        }
    }

    public HashMap<String, String> getProtocolMap() {
        return protocolMap;
    }

    public HashMap<Map.Entry<String, String>, String> getLookupTable() {
        return lookupTable;
    }
}
