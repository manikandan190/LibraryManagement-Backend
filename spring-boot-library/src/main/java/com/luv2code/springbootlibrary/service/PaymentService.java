package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.deo.PaymentRepository;

import com.luv2code.springbootlibrary.entity.Payment;
import com.luv2code.springbootlibrary.requestmodels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentService {
private PaymentRepository paymentRepository;
@Autowired
public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}") String secretKey)
{
    this.paymentRepository=paymentRepository;
    Stripe.apiKey=secretKey;
}
public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException{
//    List<String> paymentMethodTypes=new ArrayList<>();
//    paymentMethodTypes.add("card");
//    Map<String,Object> params=new HashMap<>();
//    params.put("amount",paymentInfoRequest.getAmount());
//    params.put("currency",paymentInfoRequest.getCurrency());
//    params.put("payment_method_types",paymentMethodTypes);
    PaymentIntentCreateParams params =
            PaymentIntentCreateParams.builder()
                    .setDescription("Late Fee for book")
                    .setShipping(
                            PaymentIntentCreateParams.Shipping.builder()
                                    .setName("Jenny Rosen")
                                    .setAddress(
                                            PaymentIntentCreateParams.Shipping.Address.builder()
                                                    .setLine1("510 Townsend St")
                                                    .setPostalCode("98140")
                                                    .setCity("San Francisco")
                                                    .setState("CA")
                                                    .setCountry("US")
                                                    .build()
                                    )
                                    .build()
                    )
                    .setAmount((long) paymentInfoRequest.getAmount())
                    .setCurrency(paymentInfoRequest.getCurrency())
                    .addPaymentMethodType("card")
                    .build();
    return PaymentIntent.create(params);
}
public ResponseEntity<String> stripePayment(String userEmail) throws Exception{
    Payment payment= paymentRepository.findByUserEmail(userEmail);
    if(payment==null)
    {
        throw new Exception("Payment Information is missing");
    }
    payment.setAmount(00.00);
    paymentRepository.save(payment);
    return new ResponseEntity<>(HttpStatus.OK);
}
}
