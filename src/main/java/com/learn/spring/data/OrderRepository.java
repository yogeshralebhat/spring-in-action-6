package com.learn.spring.data;

import com.learn.spring.model.TacoOrder;

public interface OrderRepository {

	TacoOrder save(TacoOrder tacoOrder);

}
