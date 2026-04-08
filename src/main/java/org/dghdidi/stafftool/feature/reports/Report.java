package org.dghdidi.stafftool.feature.reports;

import java.util.Objects;

public class Report {
    private final String playerName;
    private final String targetName;

    public Report(String playerName, String targetName) {
        this.playerName = playerName;
        this.targetName = targetName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTargetName() {
        return targetName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(playerName, report.playerName) && Objects.equals(targetName, report.targetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, targetName);
    }
}
