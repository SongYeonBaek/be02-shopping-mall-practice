package com.example.demo.order.controller;
import com.amazonaws.Response;
import com.example.demo.order.service.OrderService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.management.remote.JMXServerErrorException;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin 으로 특정 프론트만 연결 하도록 설정!! CORS 에러를 잡기 위함. *로 했으니 모두 허용해 주겠다.
@RestController
@CrossOrigin("*")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //스트림이기 때문에 예외처리 해줘야 함
    @RequestMapping(method = RequestMethod.POST,value = "/token")
    public String getToken() throws IOException {
        HttpsURLConnection conn = null;

        //아임 포트에게 토큰을 달라고 요청
        URL url = new URL("https://api.iamport.kr/users/getToken");

        //HTTP 프로토콜 만들어주기
        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        //데이터를 담아서 보내겠다
        conn.setDoOutput(true);

        JsonObject json = new JsonObject();
        json.addProperty("imp_key","0675386472841152");
        json.addProperty("imp_secret","SlX30zSyAol0IdbXYwKu13rQk44eYLaVIsp95BJ0atkuSgmmfTOHr3niwyQEWTxQAFpftryJB5DhNPVI" );

        //보내기
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
        String token = gson.fromJson(response, Map.class).get("access_token").toString();

        br.close();
        conn.disconnect();

        return token;
    }

    @RequestMapping("/get/info")
    public Map<String, String> getPaymentInfo(String impUid) throws IOException {
        String token = getToken();
        HttpsURLConnection conn = null;

        //아임 포트에게 토큰을 달라고 요청
        URL url = new URL("https://api.iamport.kr/payments/" + impUid);

        //HTTP 프로토콜 만들어주기
        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", token);
        //데이터를 담아서 보내겠다
        conn.setDoOutput(true);

        //읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        br.close();
        conn.disconnect();

        return orderService.parsing(response.toString());

    }

    //실제로 검증하는 코드
    @RequestMapping(method = RequestMethod.GET, value = "/validation")
    public ResponseEntity paymentValidation(String impUid) throws IOException {
        Map<String, String> paymentResult = getPaymentInfo(impUid);
        String dbPrice= paymentResult.get("correctPrice"); //db에서 값을 조회하는 것
        String amount = paymentResult.get("amount");


        Integer result = Math.round(Float.parseFloat(amount));
        System.out.println("amount: "+ result +"\ndbprice: "+dbPrice);
        if(result==Integer.parseInt(dbPrice)) {
            return ResponseEntity.ok().body("ok");
        }
        else {
            //금액이 같지 않다면 결제 취소 처리
            String token = getToken();
            payMentCancel(token, impUid, paymentResult.get("amount"), "결제 금액 에러" );

            return ResponseEntity.badRequest().body("error");
        }
    }

    private void payMentCancel(String token, String impUid, String amount, String reason) throws IOException {
        URL url = new URL("https://api.iamport.kr/payments/cancel");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");

        // 요청의 Content-Type, Accept, Authorization 헤더 설정
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", token);

        // 해당 연결을 출력 스트림(요청)으로 사용
        conn.setDoOutput(true);

        // JSON 객체에 해당 API가 필요로하는 데이터 추가.
        JsonObject json = new JsonObject();
        json.addProperty("imp_uid", impUid);
        json.addProperty("reason", reason);

        // 출력 스트림으로 해당 conn에 요청
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(json.toString());
        bw.flush();
        bw.close();

        // 입력 스트림으로 conn 요청에 대한 응답 반환
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        Gson gson = new Gson();
        String response = gson.fromJson(br.readLine(), Map.class).toString();

        System.out.println(response);

        br.close();
        conn.disconnect();

        System.out.println("결제 취소 완료: 주문번호 " + impUid);

    }


}
