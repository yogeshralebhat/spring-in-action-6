package com.learn.spring.data;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.learn.spring.model.Ingredient;
import com.learn.spring.model.Taco;
import com.learn.spring.model.TacoOrder;

@Repository
public class JdbcOrderRepository implements OrderRepository {
	
	private JdbcOperations jdbcOperations;
	
	public JdbcOrderRepository(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}

	@Override
	public TacoOrder save(TacoOrder tacoOrder) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"insert into Taco_Order (delivery_Name, delivery_Street, delivery_City, "
				+ "delivery_State, delivery_Zip, cc_number, cc_expiration, cc_cvv, placed_at) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
				Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP 
				);
		
		pscf.setReturnGeneratedKeys(true);
		
		tacoOrder.setPlacedAt(new Date());
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
				tacoOrder.getDeliveryName(),
				tacoOrder.getDeliveryStreet(),
				tacoOrder.getDeliveryCity(),
				tacoOrder.getDeliveryState(),
				tacoOrder.getDeliveryZip(),
				tacoOrder.getCcNumber(),
				tacoOrder.getCcExpiration(),
				tacoOrder.getCcCVV(),
				tacoOrder.getPlacedAt()));
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update(psc, keyHolder);
		
		long orderId = keyHolder.getKey().longValue();
		tacoOrder.setId(orderId);
		
		List<Taco> tacos = tacoOrder.getTacos();
		int orderKey = 0;
		for(Taco taco : tacos) {
			saveTaco(orderId, orderKey, taco);
			orderKey++;
		}
		
		return tacoOrder;
	}
	
	private long saveTaco(long orderId, int orderKey, Taco taco) {
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"insert into Taco (name, taco_order, taco_order_key, created_at) "
				+ "values (?, ?, ?, ?)",
				Types.VARCHAR, Types.BIGINT, Types.BIGINT, Types.TIMESTAMP);
		
		pscf.setReturnGeneratedKeys(true);
		
		taco.setCreatedAt(new Date());
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
				taco.getName(),
				orderId,
				orderKey,
				taco.getCreatedAt()));
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update(psc, keyHolder);
		
		long tacoId = keyHolder.getKey().longValue();
		taco.setId(tacoId);
		
		saveIngredientRefs(tacoId, taco.getIngredients());
		
		return tacoId;
	}

	private void saveIngredientRefs(long tacoId, List<Ingredient> ingredients) {
		int tacoKey = 0;
		for(Ingredient ingredient : ingredients) {
			PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
					"insert into Ingredient_Ref (ingredient, taco, taco_key) values (?, ?, ?)",
					Types.VARCHAR, Types.BIGINT, Types.BIGINT);
			
			PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
					ingredient.getId(),
					tacoId,
					tacoKey++));
			
			jdbcOperations.update(psc);
		}
	}

}
