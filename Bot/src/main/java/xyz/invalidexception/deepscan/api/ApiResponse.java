package xyz.invalidexception.deepscan.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse {
    public HashMap<String, Double> category_scores;
    public HashMap<String, Boolean> detect;
    public boolean detected;
}
