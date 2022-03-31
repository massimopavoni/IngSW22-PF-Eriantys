package it.polimi.ingsw.javangers.server.model.gameData.tokenContainers;

import it.polimi.ingsw.javangers.server.model.gameData.enums.TokenColor;

import java.util.List;
import java.util.Map;

public class StudentsBag extends TokenContainer {

    /**
     * Constructor for studentsBag initializing tokensList according to
     * the input parameter Map studentsPerColor.
     */
    public StudentsBag(Map<TokenColor, Integer> studentsPerColor) {
        super();
        for (TokenColor t :
                TokenColor.values()) {
            if (studentsPerColor.get(t) != null) {
                for (int i = 0; i < studentsPerColor.get(t); i++) {
                    tokensList.add(t);
                }
            }
        }
    }

    /**
     * Grab specified tokens list from studentsBag.
     *
     * @param condition condition to grab tokens
     * @return list of grabbed tokens
     */
    @Override
    public List<TokenColor> grabTokens(Object condition) {
        //To do...
        return null;
    }
}
