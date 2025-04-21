package dev.gmathur.sec03;

public record JsonPerson(
        String first_name,
        String last_name,
        int age,
        String email,
        Boolean is_developer,
        double  salary,
        long bank_account_number,
        int bank_balance,
        float   height
) {

}
