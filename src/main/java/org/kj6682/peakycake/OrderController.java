package org.kj6682.peakycake;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/orders")
    List<Order> listOrders(){
        return orderRepository.findAll()
                .orElseThrow(()->new OrderNotFoundException("all"));

    }


    @GetMapping(value = "/{shop}/orders")
    List<Order> listShopOrders(@PathVariable String shop){
        Assert.notNull(shop,"shop can not be null");
        return orderRepository.findByShop(shop)
                .orElseThrow(()-> new OrderNotFoundException(shop));
    }

    @PostMapping(value = "/{shop}/orders")
    void create(@PathVariable String shop, @RequestBody Order order) {
        Assert.notNull(shop,"shop can not be null");
        Assert.notNull(order, "Order can not be empty");
        orderRepository.save(order);

    }

    private static class OrderNotFoundException extends RuntimeException{
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
    }
}