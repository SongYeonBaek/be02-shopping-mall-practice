package com.example.demo.order.service;

import com.example.demo.order.model.GetOrdersRes;
import com.example.demo.order.model.Orders;
import com.example.demo.order.model.PaymentProducts;
import com.example.demo.order.repository.OrdersRepository;
import com.example.demo.order.model.OrderedProduct;
import com.example.demo.order.repository.OrderedProductRepository;
import com.example.demo.product.model.Product;
import com.example.demo.product.model.ProductDto;
import com.example.demo.product.service.ProductService;
import com.example.demo.user.model.User;
import com.google.gson.Gson;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService2 {
    private final IamportClient iamportClient;
    private final ProductService productService;
    private final OrdersRepository ordersRepository;
    private final OrderedProductRepository orderedProductRepository;

    //결제 정보를 가져옴. IamportResponse에 response 다 들어있음! (amount, customData 등)
    public IamportResponse getPaymentInfo(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
        return response;
    }

    public Boolean paymentValidation(String impUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> response = getPaymentInfo(impUid);
        Integer amount = response.getResponse().getAmount().intValue();
        String customDataString = response.getResponse().getCustomData();

        Gson gson = new Gson();
        PaymentProducts paymentProducts = gson.fromJson(customDataString, PaymentProducts.class);

        Integer totalPrice = productService.getTotalPrice(paymentProducts);

        if(amount.equals(totalPrice) ) {
            return true;
        }

        return false;

    }

    public void createOrder(String impUid) throws IamportResponseException, IOException {
        Orders order= ordersRepository.save(Orders.builder()
                        .impUid(impUid)
                        .user(User.builder().id(1L).email("test01@test.com").build())
                .build());

        IamportResponse<Payment> response = getPaymentInfo(impUid);
        String customDataString = response.getResponse().getCustomData();
        Gson gson = new Gson();
        PaymentProducts paymentProducts = gson.fromJson(customDataString, PaymentProducts.class);

        //Custom Data 안에 있던 Product 리스트 하나씩 꺼내와서 OrderedProduct에 저장
        for (Product product : paymentProducts.getProducts()) {
            orderedProductRepository.save(OrderedProduct.builder()
                    .orders(order)
                    .product(product)
                    .build());
        }
    }

    public List<GetOrdersRes> list() {
        List<Orders> ordersList = ordersRepository.findAll();
        List<GetOrdersRes> response = new ArrayList<>();

        for (Orders orders : ordersList) {
            List<ProductDto> productDtoList = new ArrayList<>();
            for (OrderedProduct orderProducts: orders.getOrderProductsList()) {

                ProductDto productDto = ProductDto.builder()
                        .id(orderProducts.getProduct().getId())
                        .name(orderProducts.getProduct().getName())
                        .price(orderProducts.getProduct().getPrice())
                        .build();

                productDtoList.add(productDto);
            }

            GetOrdersRes getOrdersRes = GetOrdersRes.builder()
                    .id(orders.getId())
                    .userName(orders.getUser().getName())
                    .products(productDtoList)
                    .build();
            response.add(getOrdersRes);
        }

        return response;
    }
}
