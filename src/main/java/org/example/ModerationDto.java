package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ModerationDto {
    private List<Result> results;

    @Data
    public static class Result {
        private List<Categorie> categories;
        private CategoryScore categoryScores;
    }
}
