package com.kream.root.order.service;

import com.kream.root.Login.model.UserListDTO;
import com.kream.root.Login.repository.UserListRepository;
import com.kream.root.MainAndShop.domain.Product;
import com.kream.root.MainAndShop.repository.ProductRepository;
import com.kream.root.entity.*;
import com.kream.root.order.PaymentInfo;
import com.kream.root.order.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private CancellationRepository cancellationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("ApiKey")
    private String apiKey;

    @Value("ApiSecret")
    private String apiSecret;

    @Autowired
    private ProductRepository productRepository;
    public Orders createOrder(PaymentInfo paymentInfo, UserListDTO user, AddressBook addressBook) {
        // OrderItems 목록 생성
        List<OrderItems> orderItemsList = new ArrayList<>();
        for (int i = 0; i < paymentInfo.getProductIds().size(); i++) {
            Long productId = paymentInfo.getProductIds().get(i);
            int quantity = paymentInfo.getQuantities().get(i);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

            OrderItems orderItem = OrderItems.builder()
                    .product(product)
                    .quantity(quantity)
                    .price(product.getPrice())  // Assume product has a getPrice() method
                    .build();

            orderItemsList.add(orderItem);
        }

        Orders order = Orders.builder()
                .orderCode(generateOrderCode())
                .orderDate(LocalDateTime.now())
                .user(user)
                .sellerProduct(new SellerProduct())  // Assign seller product based on payment info if needed
                .orderItems(orderItemsList)
                .applyNum(paymentInfo.getApplyNum())
                .bankName(paymentInfo.getBankName())
                .buyerAddr(paymentInfo.getBuyerAddr())
                .buyerEmail(paymentInfo.getBuyerEmail())
                .buyerName(paymentInfo.getBuyerName())
                .buyerPostcode(paymentInfo.getBuyerPostcode())
                .buyerTel(paymentInfo.getBuyerTel())
                .cardName(paymentInfo.getCardName())
                .cardNumber(paymentInfo.getCardNumber())
                .cardQuota(paymentInfo.getCardQuota())
                .currency(paymentInfo.getCurrency())
                .customData(paymentInfo.getCustomData())
                .impUid(paymentInfo.getImpUid())
                .merchantUid(paymentInfo.getMerchantUid())
                .productName(paymentInfo.getProductName())
                .paidAmount(paymentInfo.getPaidAmount())
                .paidAt(paymentInfo.getPaidAt())
                .payMethod(paymentInfo.getPayMethod())
                .pgProvider(paymentInfo.getPgProvider())
                .pgTid(paymentInfo.getPgTid())
                .pgType(paymentInfo.getPgType())
                .receiptUrl(paymentInfo.getReceiptUrl())
                .status(paymentInfo.getStatus())
                .success(paymentInfo.isSuccess())
                .build();

        // Save order to obtain the orderId
        Orders savedOrder = ordersRepository.save(order);

        // Update orderItems with the saved order
        for (OrderItems item : orderItemsList) {
            item.setOrder(savedOrder);
            orderItemsRepository.save(item);
        }

        // Create delivery
        Delivery delivery = Delivery.builder()
                .deliveryStatus("주문된 상태")
                .deliveryAddress(addressBook.getCity() + " " + addressBook.getStreet())
                .order(savedOrder)
                .build();
        deliveryRepository.save(delivery);

        return savedOrder;
    }

    public void handlePayment(PaymentInfo paymentInfo) {
        if ("paid".equals(paymentInfo.getStatus()) && paymentInfo.isSuccess()) {
            UserListDTO user = getUserById(paymentInfo.getUserId());
            List<AddressBook> sortedAddressBooks = getSortedAddressBooksByUserId(paymentInfo.getUserId());
            AddressBook addressBook = sortedAddressBooks.get(0);

            createOrder(paymentInfo, user, addressBook);
        }
    }

    public UserListDTO getUserById(int userId) {
        return userListRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }

    public List<AddressBook> getSortedAddressBooksByUserId(int userId) {
        List<AddressBook> addressBooks = addressBookRepository.findByUserUlid(userId);
        return sortAddressBooks(addressBooks);
    }

    private List<AddressBook> sortAddressBooks(List<AddressBook> addressBooks) {
        List<AddressBook> sortedList = new ArrayList<>();
        AddressBook defaultAddress = null;

        for (AddressBook address : addressBooks) {
            if (address.getIsDefault() == '1') {
                defaultAddress = address;
            } else {
                sortedList.add(address);
            }
        }

        if (defaultAddress != null) {
            sortedList.add(0, defaultAddress);
        }

        return sortedList;
    }

    private String generateOrderCode() {
        // Generate a unique order code
        return "ORD" + System.currentTimeMillis();
    }
    public String getToken() throws Exception {
        String url = "https://api.iamport.kr/users/getToken";
        Map<String, String> body = new HashMap<>();
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCodeValue() == 200) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, String> responseMap = (Map<String, String>) responseBody.get("response");
            return responseMap.get("access_token");
        } else {
            throw new Exception("Failed to get token");
        }
    }

    public void cancelPayment(String impUid, Long merchantUid, int userId) throws Exception {
        String token = getToken();
        String url = "https://api.iamport.kr/payments/cancel";

        Orders order = ordersRepository.findById(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));

        UserListDTO user = userListRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        RefundAccount refundAccount = user.getRefundAccount();

        Map<String, Object> body = new HashMap<>();
        body.put("imp_uid", impUid);
        body.put("merchant_uid", merchantUid.toString());
        body.put("amount", order.getPaidAmount());
        body.put("tax_free", 0);
        body.put("vat_amount", 0);
        body.put("checksum", order.getPaidAmount());
        body.put("reason", "테스트 결제");
        body.put("refund_holder", refundAccount.getAccountHolder());
        body.put("refund_bank", refundAccount.getBankCode());
        body.put("refund_account", refundAccount.getAccountNumber());
        body.put("refund_tel", refundAccount.getContactNumber());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("Authorization", token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCodeValue() == 200) {
            Cancellation cancellation = Cancellation.builder()
                    .order(order)
                    .user(user)
                    .impUid(impUid)
                    .merchantUid(merchantUid)
                    .amount(order.getPaidAmount())
                    .reason("테스트 결제")
                    .cancelledAt(LocalDateTime.now())
                    .refundHolder(refundAccount.getAccountHolder())
                    .refundBank(refundAccount.getBankCode())
                    .refundAccount(refundAccount.getAccountNumber())
                    .refundTel(refundAccount.getContactNumber())
                    .build();

            cancellationRepository.save(cancellation);
        } else {
            throw new Exception("Failed to cancel payment");
        }
    }

}
