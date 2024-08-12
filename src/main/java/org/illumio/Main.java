package org.illumio;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlowLogReader flowLogReader = new FlowLogReader();
        flowLogReader.parseFlowLogFileAndPopulateResultFiles();
    }
}