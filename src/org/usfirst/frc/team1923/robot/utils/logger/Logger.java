package org.usfirst.frc.team1923.robot.utils.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {

    private boolean active;

    private StringBuilder log;
    private ConcurrentHashMap<String, DataSource> dataSources;
    private ConcurrentHashMap<String, List<TransientDataSource>> transientDataSources;

    public Logger() {
        this.active = false;

        this.dataSources = new ConcurrentHashMap<>();
        this.transientDataSources = new ConcurrentHashMap<>();

        this.addDataSource("Timestamp", () -> System.currentTimeMillis() + "");
    }

    public void initialize() {
        this.active = true;

        this.log = new StringBuilder();

        StringBuilder line = new StringBuilder();

        for (String key : this.dataSources.keySet()) {
            line.append("\"").append(key).append("\",");
        }

        for (String key : this.transientDataSources.keySet()) {
            line.append("\"").append(key).append("\",");
        }

        this.log.append(line.toString().replaceAll(",$", "")).append("\n");
    }

    public boolean isActive() {
        return this.active;
    }

    public void save() {
        this.active = false;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.csv"));

            writer.write(this.log.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDataSource(String name, DataSource dataSource) {
        name = name.replaceAll("[^A-Za-z0-9_]", "");

        if (this.dataSources.containsKey(name) || this.transientDataSources.containsKey("name")) {
            throw new RuntimeException("Key " + name + " already exists!");
        }

        this.dataSources.put(name, dataSource);
    }

    public void addTransientDataSource(String name, TransientDataSource dataSource) {
        name = name.replaceAll("[^A-Za-z0-9_]", "");

        if (this.dataSources.containsKey(name)) {
            throw new RuntimeException("Key " + name + " already exists!");
        }

        if (!this.transientDataSources.containsKey(name)) {
            this.transientDataSources.put(name, new ArrayList<>());
        }

        this.transientDataSources.get(name).add(dataSource);
    }

    public void periodic() {
        StringBuilder line = new StringBuilder();

        for (DataSource source : this.dataSources.values()) {
            line.append("\"").append(source.getData().replaceAll("[^A-Za-z0-9_.]", "")).append("\",");
        }

        for (List<TransientDataSource> transientDataSources : this.transientDataSources.values()) {
            boolean filled = false;

            for (TransientDataSource source : transientDataSources) {
                if (source.isActive()) {
                    line.append("\"").append(source.getData().replaceAll("[^A-Za-z0-9_.]", "")).append("\",");

                    filled = true;

                    break;
                }
            }

            if (!filled) {
                line.append("\"\",");
            }
        }

        this.log.append(line.toString().replaceAll(",$", "")).append("\n");
    }

}
