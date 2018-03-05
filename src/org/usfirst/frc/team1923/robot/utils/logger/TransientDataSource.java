package org.usfirst.frc.team1923.robot.utils.logger;

public class TransientDataSource {

    private DataSource dataSource;
    private TransientDataSourceStatus status;

    public TransientDataSource(DataSource dataSource, TransientDataSourceStatus status) {
        this.dataSource = dataSource;
        this.status = status;
    }

    public boolean isActive() {
        return this.status.isActive();
    }

    public String getData() {
        return this.dataSource.getData();
    }

    public interface TransientDataSourceStatus {

        public boolean isActive();

    }

}
