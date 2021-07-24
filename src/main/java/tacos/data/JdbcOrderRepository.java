//package tacos.data;
//
//import org.springframework.asm.Type;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import tacos.Ingredient;
//import tacos.IngredientRef;
//import tacos.Taco;
//import tacos.TacoOrder;
//
//import java.sql.Types;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//@Repository
//public class JdbcOrderRepository implements OrderRepository {
//
//    private JdbcTemplate jdbcTemplate;
//
//    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    @Transactional
//    public TacoOrder save(TacoOrder order) {
//        var pscf = new PreparedStatementCreatorFactory(
//                "INSERT INTO Taco_Order "
//                        + "(delivery_name, delivery_street, delivery_city, "
//                        + "delivery_state, delivery_zip, cc_number, "
//                        + "cc_expiration, cc_cvv, places_at)"
//                        + "VALUES (?,?,?,?,?,?,?,?,?)",
//                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
//                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
//                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
//        );
//        pscf.setReturnGeneratedKeys(true);
//
//        order.setPlacedAt(new Date());
//        var psc = pscf.newPreparedStatementCreator(
//                Arrays.asList(
//                        order.getDeliveryName(),
//                        order.getDeliveryStreet(),
//                        order.getDeliveryCity(),
//                        order.getDeliveryState(),
//                        order.getDeliveryZip(),
//                        order.getCcNumber(),
//                        order.getCcExpiration(),
//                        order.getCcCVV(),
//                        order.getPlacedAt()));
//
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(psc, keyHolder);
//        long orderId = keyHolder.getKey().longValue();
//        order.setId(orderId);
//
//        List<Taco> tacos = order.getTacos();
//        int i = 0;
//        for (Taco taco : tacos) {
//            saveTaco(orderId, i++, taco);
//        }
//
//        return order;
//    }
//
//    private long saveTaco(Long orderId, int orderKey, Taco taco) {
//        taco.setCreatedAt(new Date());
//        var pscf = new PreparedStatementCreatorFactory(
//                "INSERT INTO Taco "
//                + "(name, created_at, taco_order, taco_order_key) "
//                + "VALUES (?,?,?,?)",
//                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
//        );
//        pscf.setReturnGeneratedKeys(true);
//
//        var psc = pscf.newPreparedStatementCreator(
//                Arrays.asList(
//                        taco.getName(),
//                        taco.getCreatedAt(),
//                        orderId,
//                        orderKey));
//
//        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(psc, keyHolder);
//        long tacoId = keyHolder.getKey().longValue();
//        taco.setId(tacoId);
//
//        saveIngredientRef(tacoId, taco.getIngredients());
//
//        return tacoId;
//    }
//
//    private void saveIngredientRef(long tacoId, List<IngredientRef> ingredientRefs) {
//        int key = 0;
//        for (IngredientRef ingredientRef : ingredientRefs) {
//            jdbcTemplate.update(
//                    "INSERT INTO Ingredient_Ref (ingredient, taco, taco_key) "
//                    + "values (?,?,?)",
//                    ingredientRef.getIngredient(), tacoId, key++);
//        }
//    }
//}
