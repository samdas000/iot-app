package com.sam.iot.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.toilelibre.libe.curl.Curl.curl;

@RestController
public class IoTController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/info")
    public String getinfo() {
        return "IoT Application v1.0";
    }

    @RequestMapping("/data")
    public void postSensorData(@RequestBody String data) {
        System.out.println(">>" + data);

        //curl("-i -XPOST 'http://localhost:8086/write?db=sensor' --data-binary 'data,type=temp,sensor_id=BBB value=34 1483481572000000000'");
        //curl  -i -XPOST 'http://localhost:8086/write?db=sensor' --data-binary 'data,type=temp,sensor_id=BBB value=34 1483481572000000000'
    }

    @RequestMapping("/data1")
    public void postSensorData1(@RequestBody Map<String, Object> payload) {
        System.out.println(">>" + payload);
        System.out.println("1>>" + payload.get("ts"));
        System.out.println("1>>" + payload.get("type"));
        System.out.println("1>>" + payload.get("value"));
        System.out.println("1>>" + payload.get("sensor_id"));

        System.out.println("X>>" + System.currentTimeMillis());

        String data_payload="'data,type="+ payload.get("type")
                + ",value=" + payload.get("value")
                + " sensor_id=" + payload.get("sensor_id")
                    + " " + payload.get("ts") + "'";

        String command = "-k -XPOST 'http://localhost:8086/write?db=sensor' --data-binary " + data_payload;
        System.out.println("command ]" + command);

        curl(command);

        //curl("-i -XPOST 'http://localhost:8086/write?db=sensor' --data-binary 'data,type=temp,sensor_id=BBB value=34 1483481572000000000'");
        //curl("-i -X POST 'http://localhost:8086/write?db=sensor' --data-binary " + data_payload);

    }

    @RequestMapping("/data2")
    public void postSensorData2(@RequestParam(value="data", defaultValue="World") String data) {
        System.out.println(">>" + data);
        //'{"ts":"'$d'", "type": "temp", "value": '$temp', "sensor_id": 123 }
        //data,type=temp,sensor_id=AAA value=34 1483481572000000000
    }





}
