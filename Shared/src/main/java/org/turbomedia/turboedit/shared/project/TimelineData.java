package org.turbomedia.turboedit.shared.project;

import java.util.ArrayList;

public record TimelineData(String name, long length, ArrayList<TimelineLayer> videoLayers,
                           ArrayList<TimelineLayer> audioLayers, ArrayList<TimelineClip> clips) {
}
