package org.example;

import lombok.Data;

@Data
public class CategoryScore {
    private Long sexual;                 // Score for sexual content
    private Long hate;                   // Score for hate speech
    private Long harassment;             // Score for harassment
    private Long selfHarm;               // Score for self-harm content
    private Long sexualMinors;           // Score for content involving sexual exploitation of minors
    private Long hateThreatening;        // Score for hate content with threats
    private Long violenceGraphic;        // Score for graphic violence
    private Long selfHarmIntent;         // Score for self-harm intent content
    private Long selfHarmInstructions;   // Score for content with instructions on self-harm
    private Long harassmentThreatening;  // Score for threatening harassment
    private Long violence;               // Score for general violence
}