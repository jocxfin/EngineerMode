package com.android.engineeringmode.wifimtk;

class ChannelInfo {
    final int[] mChannelFreq = new int[]{2412000, 2417000, 2422000, 2427000, 2432000, 2437000, 2442000, 2447000, 2452000, 2457000, 2462000, 2467000, 2472000, 2484000};
    int mChannelIndex = 0;
    final String[] mChannelName = new String[]{"Channel 1", "Channel 2", "Channel 3", "Channel 4", "Channel 5", "Channel 6", "Channel 7", "Channel 8", "Channel 9", "Channel 10", "Channel 11", "Channel 12", "Channel 13", "Channel 14"};

    ChannelInfo() {
    }

    int getChannelNumber() {
        return this.mChannelName.length;
    }

    int getChannelFreq() {
        return this.mChannelFreq[this.mChannelIndex];
    }
}
