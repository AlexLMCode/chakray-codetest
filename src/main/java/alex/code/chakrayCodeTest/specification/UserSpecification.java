package alex.code.chakrayCodeTest.specification;

import org.springframework.data.jpa.domain.Specification;

import alex.code.chakrayCodeTest.model.User;

public class UserSpecification {
    public static Specification<User> buildFilter(String filter){
        return (root, query, criteriaBuilder) -> {
            if (filter == null || filter.isBlank()) return null;

            String[] parts = filter.split("\\s+", 3);

            if (parts.length != 3) return null;

            String field = snakeToCamel(parts[0]);
            String operator = parts[1];
            String value = parts[2];

            return switch (operator){
                // contains
                case "co" -> criteriaBuilder.like(root.get(field), "%" + value + "%");
                // equals
                case "eq" -> criteriaBuilder.equal(root.get(field), value);
                // starts with
                case "sw" -> criteriaBuilder.like(root.get(field), value + "%");
                //ends with
                case "ew" -> criteriaBuilder.like(root.get(field), "%" + value);
                default -> null;
            };
        };
    }

    private static String snakeToCamel(String snake) {
        // splits string by "-"
        String[] words = snake.split("_");

        // starts with the first word
        StringBuilder sb = new StringBuilder(words[0]);

        // for each next word, cappitalices the first letter and concatenate the rest
        for (int i = 1; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            sb.append(words[i].substring(1));
        }
        return sb.toString();
    }
}
