# Vehicle Service Management

## Prerequisites
- Ensure that the Excel file `vehicle.xlsx` is placed in the `/home/ubuntu/excel/` directory.

## Service Management

### Stopping the Service
To stop the service running on port 8080, use the following command:
```bash
sudo kill -9 $(sudo lsof -t -i:8080)
```

### Starting the Service
To start the service, follow these steps:
1. Navigate to the target directory:
   ```shell
   cd /home/ubuntu/vehicle-service/target
   ```
2. Run the service using `nohup`:
   ```shell
   nohup java -jar vehicle-service-0.0.1-SNAPSHOT.jar &
   ```
3. Press `Enter` twice to detach the process from the terminal.

### Viewing Logs
To view the logs, use the following command:
```bash
tail -f nohup.out
```
to see old logs
```bash
tail -100f nohup.out
```
where 100 is the amount of previous lines you want to see the logs of.
You can change it to your requirement.

## Configuration

### Changing Mail Sending Time
To change the time for sending mails, edit the `application.yml` file located at
`/home/ubuntu/vehicle-service/src/main/resources/application.yml`.

```yaml
cron:
  time: 00 00 01 * * *
  zone: IST
```

Modify the `cron` time using the 24-hour format as shown below:
- `seconds [0-60]`
- `minutes [0-60]`
- `hour [0-24]`
- `* * *` indicates the task runs daily

For example, to set the time to 2:30 AM, you would use:
```yaml
cron:
  time: 00 30 02 * * *
  zone: IST
```

## Restarting the Service
If you make changes to the `application.yml` file or any other configuration, follow these steps to restart the service:

1. Stop the service:
   ```bash
   sudo kill -9 $(sudo lsof -t -i:8080)
   ```
2. Navigate to the project directory:
   ```bash
   cd /home/ubuntu/vehicle-service
   ```
3. Build the project:
   ```bash
   mvn clean install -DskipTests
   ```
4. Navigate to the target directory:
   ```bash
   cd target
   ```
5. Start the service:
   ```bash
   nohup java -jar vehicle-service-0.0.1-SNAPSHOT.jar &
   ```
6. View the logs:
   ```bash
   tail -f nohup.out
   ```
