## Setup Sensor Database 
### Pull docker image of Influx DB and generate config file

Use the following command to pull a docker image of Influx DB and generate the default configuration file
```
docker container run --rm influxdb influxd config > influxdb.conf
```
### Edit the admin part of the config file to enable and view the admin interface
```
[admin]
  enabled = true
  bind-address = ":8083"
  https-enabled = false
  https-certificate = "/etc/ssl/influxdb.pem"
```

### Use above  config file to run InfluxDB image
``` 
docker container run --rm -p 8083:8083 -p 8086:8086 \
      -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
      -v influxdata:/var/lib/influxdb \
      --name influx \
      influxdb -config /etc/influxdb/influxdb.conf

OR

// Use this one to protect database with userId and password
$ docker run --rm -p 8083:8083 -p 8086:8086 \
      -e INFLUXDB_DB=db0 \
      -e INFLUXDB_ADMIN_USER=admin -e INFLUXDB_ADMIN_PASSWORD=pass \
      -e INFLUXDB_USER=user -e INFLUXDB_USER_PASSWORD=pass \
      -v $PWD/influxdb.conf:/etc/influxdb/influxdb.conf:ro \
      -v $PWD:/var/lib/influxdb \
      influxdb -config /etc/influxdb/influxdb.conf

```

### Influx DB API reference
https://archive.docs.influxdata.com/influxdb/v1.2/tools/api/#write


### Create a sensor database
```
curl -i -XPOST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE sensor"
```

### Test inserting and retrieving data to/from database 
```
curl -i -XPOST 'http://localhost:8086/write?db=sensor' --data-binary 'data,type=temp,sensor_id=AAA value=34 1483481572000000000'
curl -i -XPOST 'http://localhost:8086/query?db=sensor' --data-urlencode 'q=select * from "data"'


curl -i -XPOST 'http://localhost:8086/write --data-binary CREATE RETENTION POLICY "a_year" ON "sensor" DURATION 52w REPLICATION 1
curl -i -XPOST http://localhost:8086/query --data-urlencode "q=CREATE RETENTION POLICY "one_week_only" ON "sensor" DURATION 1w REPLICATION 1 DEFAULT"

```


## Start the simulator
Run the simulator 
- Go to simulator directory and find the shell script
- Run the simulator shell script: ./simulator.sh
- You can change the simulator Id (a.k.a sensor Id by providing command line parameter: ./simulator.sh <AAA>  - here <AAA> is the simulator Id)

As soon as you start the simulator - simulator will start sending sensor data to IoT Hub/service every one second.
In initial design we have added just one parameter - temperature (temp) and it's value - which is sent to the IoT Hub/service 


## Create a Service to receive & process sensor data

We have created a service to receive the data sent by the sensors. The service is built using Java Springboot framework.
The source code of service is inside src folder, you can build and run it using pom.xml in the root folder (the folder containing this page).

```
mvn spring-boot:run

```

This service will listen in port 8080 for sensor data posted by sensors/gateways (in this application by the simulator application)


## Visualization of data received from sensors

### Pull out image of Graphana
Pull out published image of Graphana
```
docker container run -p 3000:3000 grafana/grafana
```
Login to Graphana application - admin/admin






