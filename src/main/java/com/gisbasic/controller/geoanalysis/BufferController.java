package com.gisbasic.controller.geoanalysis;

import com.alibaba.fastjson.JSONObject;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 缓冲区分析控制器
 *
 * @author homxu
 * @date 5/29/19
 * @help http://docs.geotools.org/latest/userguide/unsupported/geojson.html (geojson to Geometry)
 */
@RestController
@RequestMapping("geoanalysis")
public class BufferController {

    @PostMapping(value = "buffer")
    public String buffer(String geojson,Double distance) throws IOException, ParseException {

        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geojson);

        //设置buffer距离
        double degree = distance / (2 * Math.PI * 6371004) * 360;

        //将json字符串转为json对象
        JSONObject jsonObject = JSONObject.parseObject(geojson);
        String type = (String) jsonObject.get("type");

        Geometry result = null;
        //对geojson的类型进行判断
        if(type.equals("Point") ){
            result = gjson.readPoint(reader);
        }
        else if(type.equals("LineString")){
            result = gjson.readLine(reader);
        }else if(type.equals("Polygon") ){
            result = gjson.readPolygon(reader);
        }else if(type.equals("MultiPoint") ){
            result = gjson.readMultiPoint(reader);
        }else if(type.equals("MultiLineString")){
            result = gjson.readMultiLine(reader);
        }else if(type.equals("MultiPolygon")) {
            result = gjson.readPolygon(reader);
        }
        reader.close();
        //建立缓冲区
        BufferOp bufOp = new BufferOp(result);
        //设置缓冲区类型
        bufOp.setEndCapStyle( BufferParameters.CAP_ROUND);
        //设置缓冲距离
        Geometry bg = bufOp.getResultGeometry(degree);

        //将缓冲区转为geojson
        WKTReader readResult = new WKTReader();
        Geometry geometry = readResult.read(bg.toText());
        StringWriter jsonWriter = new StringWriter();
        GeometryJSON g = new GeometryJSON(6);
        g.write(geometry, jsonWriter);

        return jsonWriter.toString();
    }

}
