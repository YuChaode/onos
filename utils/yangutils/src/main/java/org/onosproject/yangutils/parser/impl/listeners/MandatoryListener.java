/*
 * Copyright 2016 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onosproject.yangutils.parser.impl.listeners;

import org.onosproject.yangutils.datamodel.YangLeaf;
import org.onosproject.yangutils.parser.Parsable;
import org.onosproject.yangutils.parser.ParsableDataType;
import org.onosproject.yangutils.parser.antlrgencode.GeneratedYangParser;
import org.onosproject.yangutils.parser.exceptions.ParserException;
import org.onosproject.yangutils.parser.impl.TreeWalkListener;
import org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorLocation;
import org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorMessageConstruction;
import org.onosproject.yangutils.parser.impl.parserutils.ListenerErrorType;
import org.onosproject.yangutils.parser.impl.parserutils.ListenerValidation;

/*
 * Reference: RFC6020 and YANG ANTLR Grammar
 *
 * ABNF grammar as per RFC6020
 *  mandatory-stmt      = mandatory-keyword sep
 *                        mandatory-arg-str stmtend
 *
 *  mandatory-arg-str   = < a string that matches the rule
 *                          mandatory-arg >
 *
 *  mandatory-arg       = true-keyword / false-keyword
 *
 * ANTLR grammar rule
 *  mandatoryStatement : MANDATORY_KEYWORD (TRUE_KEYWORD | FALSE_KEYWORD) STMTEND;
 */

/**
 * Implements listener based call back function corresponding to the "mandatory"
 * rule defined in ANTLR grammar file for corresponding ABNF rule in RFC 6020.
 */
public final class MandatoryListener {

    /**
     * Creates a new mandatory listener.
     */
    private MandatoryListener() {
    }

    /**
     * It is called when parser receives an input matching the grammar
     * rule (mandatory), performs validation and updates the data model
     * tree.
     *
     * @param listener listener's object.
     * @param ctx context object of the grammar rule.
     */
    public static void processMandatoryEntry(TreeWalkListener listener,
                                          GeneratedYangParser.MandatoryStatementContext ctx) {

        // Check for stack to be non empty.
        ListenerValidation.checkStackIsNotEmpty(listener, ListenerErrorType.MISSING_HOLDER,
                ParsableDataType.MANDATORY_DATA, "", ListenerErrorLocation.ENTRY);

        Parsable tmpNode = listener.getParsedDataStack().peek();
        switch (tmpNode.getParsableDataType()) {
            case LEAF_DATA:
                YangLeaf leaf = (YangLeaf) tmpNode;
                if (ctx.TRUE_KEYWORD() != null) {
                    leaf.setMandatory(true);
                } else {
                    leaf.setMandatory(false);
                }
                break;
            case CHOICE_DATA: // TODO
                break;
            default:
                throw new ParserException(ListenerErrorMessageConstruction
                        .constructListenerErrorMessage(ListenerErrorType.INVALID_HOLDER,
                                ParsableDataType.MANDATORY_DATA, "", ListenerErrorLocation.ENTRY));
        }
    }
}