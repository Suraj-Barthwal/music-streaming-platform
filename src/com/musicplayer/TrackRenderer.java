package com.musicplayer;

import javax.swing.*;
import java.awt.*;

public class TrackRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Track) {
            Track track = (Track) value;
            label.setText("<html><div style='padding:5px;'><b>" + track.title + "</b><br>" + track.artist + " (" + track.getFormattedDuration() + ")</div></html>");
            label.setIcon(track.icon);
            label.setIconTextGap(15);
        }
        return label;
    }
}