package org.example;

import lombok.Data;

@Data
public class Categorie {
    private boolean sexual;              // Flag for sexual content
    private boolean hate;                // Flag for hate speech
    private boolean harassment;          // Flag for harassment
    private boolean selfHarm;            // Flag for self-harm content
    private boolean sexualMinors;        // Flag for content involving sexual exploitation of minors
    private boolean hateThreatening;     // Flag for hate content with threats
    private boolean violenceGraphic;     // Flag for graphic violence
    private boolean selfHarmIntent;      // Flag for self-harm intent content
    private boolean selfHarmInstructions;// Flag for content with instructions on self-harm
    private boolean harassmentThreatening;// Flag for threatening harassment
    private boolean violence;            // Flag for general violence
}
