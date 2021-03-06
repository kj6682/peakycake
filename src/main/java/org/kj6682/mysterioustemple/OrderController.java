package org.kj6682.mysterioustemple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by luigi on 13/07/2017.
 */

@RestController
@RequestMapping("/api")
class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(value = "/orders/orders.csv")
    public void listOrdersAsCSV(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv; charset=utf-8");

        List<Order> orders = orderRepository.findAll().get();
        StringBuilder sb = new StringBuilder(Order.csvHeader());
        for(Order order : orders){
            sb.append(order.asCsv());
        }
        response.getWriter().print(sb.toString());
    }

    @GetMapping("/orders")
    List<Order> listOrders() {
        return orderRepository.findAll()
                .orElseThrow(() -> new OrderNotFoundException("all"));

    }

    @PutMapping(value = "/orders/{id}")
    ResponseEntity<?> updateStatus(@PathVariable(required = true) Long id) {

        Order result = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(String.valueOf(id)));
        result.setStatus("DONE");
        result = orderRepository.save(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id}")
    void deleteOrder(@PathVariable(required = true) Long id) {
        orderRepository.delete(id);
    }

    @GetMapping(value = "/{shop}/orders")
    List<Order> listShopOrders(@PathVariable String shop) {
        Assert.notNull(shop, "shop can not be null");

        return orderRepository.findByShop(shop)
                .orElseThrow(() -> new OrderNotFoundException(shop));
    }

    @PostMapping(value = "/{shop}/orders")
    ResponseEntity<?> create(@PathVariable String shop, @RequestBody Order order) {

        Assert.notNull(shop, "shop can not be null");
        Assert.notNull(order, "Order can not be empty");
        Assert.isTrue(order.getShop().equals(shop), "shops must be coherent");

        Order result = orderRepository.save(order);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }


    private static class OrderNotFoundException extends RuntimeException {
        OrderNotFoundException(String userId) {
            super("could not find order '" + userId + "'.");
        }
    }

    @ControllerAdvice
    private static class OrderControllerAdvice {

        @ResponseBody
        @ExceptionHandler(OrderNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public List<Order> handleConflict() {
            return new LinkedList<Order>();
        }


        @ResponseBody
        @ExceptionHandler(java.lang.IllegalArgumentException.class)
        public ResponseEntity<?> handleConflictIllegalArgument() {
            return new ResponseEntity<>("Illegal Arguments", HttpStatus.FORBIDDEN);
        }

    }
}
