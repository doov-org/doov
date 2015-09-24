/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.gen.processor;

import java.text.MessageFormat;
import java.util.*;

/**
 * Substitution récursive de variables ${...} dans un template (à la script Ant).
 */
public class MacroProcessor {

    private static final int MAX_DEPTH = 5;
    public static final String REF_PREFIX = "${";
    public static final String REF_SUFFIX = "}";
    private static final String STR_MORE_THAN_0_LEVEL_TO_EXPAND_1 = "There is more than {0} level to expand the " +
                    "property : ''{1}'')";
    private static final String STR_SYNTAX_ERROR_IN_0 = "Syntax error in property: ''{0}''";
    private static final MessageFormat MORE_THAN_0_LEVEL_TO_EXPAND_1 = new MessageFormat(
                    STR_MORE_THAN_0_LEVEL_TO_EXPAND_1);
    private static final Evaluator DEFAULT_STRING_EVAL = new DefaultEvaluator();

    @SuppressWarnings("unchecked")
    public static String replaceProperties(String value, Map<String, ?> conf, MacroParamProcessor paramProcessor)
                    throws PropertyParsingException {
        return replacePropertiesRec(value, (Map<String, Object>) conf, new ArrayList<>(),
                        new ArrayList<>(), 0, null, paramProcessor, DEFAULT_STRING_EVAL);
    }

    public static String replaceProperties(String value, Map<String, ?> conf, String defaultReplacement)
                    throws PropertyParsingException {
        return replacePropertiesRec(value, conf, new ArrayList<>(), new ArrayList<>(), 0,
                        defaultReplacement);
    }

    @SuppressWarnings("unchecked")
    public static String replaceProperties(String value, Map<String, ?> conf, String defaultReplacement,
                    Evaluator evaluator) throws PropertyParsingException {
        return replacePropertiesRec(value, (Map<String, Object>) conf, new ArrayList<>(),
                        new ArrayList<>(), 0, defaultReplacement, null, evaluator);
    }

    @SuppressWarnings("unchecked")
    public static String replaceProperties(String value, Map<String, ?> conf, int depth)
                    throws PropertyParsingException {
        return replacePropertiesRec(value, (Map<String, Object>) conf, new ArrayList<>(),
                        new ArrayList<>(), depth, null, null, DEFAULT_STRING_EVAL);
    }

    /**
     * Macro-expand parameter references <code>${xx}</code>.<br> If a reference is not found, the value of {@code param}
     * is used if it is not null - otherwise referenc is not replaced.
     *
     * @param value              input string
     * @param conf               map containing all known parameters
     * @param fragments
     * @param propertyRefs
     * @param depth              recursivity index, limited to {@value MacroProcessor#MAX_DEPTH}.
     * @param defaultReplacement
     * @return macro-expanded value.
     * @throws PropertyParsingException if recursivity goes beyond {@value MacroProcessor#MAX_DEPTH} limit.
     */
    @SuppressWarnings("unchecked")
    public static String replacePropertiesRec(String value, Map<String, ?> conf, List<String> fragments,
                    List<String> propertyRefs, int depth, String defaultReplacement)
                    throws PropertyParsingException {
        return replacePropertiesRec(value, (Map<String, Object>) conf, fragments, propertyRefs, depth,
                        defaultReplacement, null, DEFAULT_STRING_EVAL);
    }

    private static String replacePropertiesRec(String value, Map<String, Object> conf, List<String> fragments,
                    List<String> propertyRefs, int depth, String defaultReplacement,
                    MacroParamProcessor paramProcessor, Evaluator evaluator) throws PropertyParsingException {
        parsePropertyString(value, fragments, propertyRefs);
        final StringBuilder unkownParam = new StringBuilder();
        final StringBuilder sb = new StringBuilder();
        final Iterator<String> i = fragments.iterator();
        final Iterator<String> j = propertyRefs.iterator();
        while (i.hasNext()) {
            String fragment = i.next();
            if (fragment == null) {
                final String propertyName = j.next();
                fragment = evaluator.eval(conf, propertyName, defaultReplacement, paramProcessor);
                final boolean sameProperty = fragment.contains(REF_PREFIX + propertyName + REF_SUFFIX);
                final boolean alwaysProperty = containProperty(fragment);
                if (alwaysProperty && !sameProperty) {
                    unkownParam.append(fragment);
                }
            }
            sb.append(fragment);
        }
        final boolean containProperty = containProperty(unkownParam.toString());
        final String expandedValue = sb.toString();
        if (containProperty && depth <= MAX_DEPTH) {
            fragments.clear();
            propertyRefs.clear();
            return replacePropertiesRec(expandedValue, conf, fragments, propertyRefs, depth + 1,
                            defaultReplacement, paramProcessor, evaluator);
        } else if (containProperty && depth > MAX_DEPTH) {
            throw new PropertyParsingException(MORE_THAN_0_LEVEL_TO_EXPAND_1.format(new Object[] { MAX_DEPTH,
                            expandedValue }));
        } else {
            return expandedValue;
        }

    }

    /**
     * Parses a string containing <code>${xxx}</code> style property references into two lists. The first list is a
     * collection of text fragments, while the other is a set of string property names. <code>null</code> entries in the
     * first list indicate a property reference from the second list. <p> It can be overridden with a more efficient or
     * customized version.
     *
     * @param textToParse  Text to parse. Must not be <code>null</code>.
     * @param fragments    List to add text fragments to. Must not be <code>null</code>.
     * @param propertyRefs List to add property names to. Must not be <code>null</code>.
     * @throws PropertyParsingException if the string contains an opening <code>${</code> without a closing
     *                                  <code>}</code>
     */
    public static void parsePropertyString(String textToParse, List<String> fragments, List<String> propertyRefs)
                    throws PropertyParsingException {
        final MessageFormat SYNTAX_ERROR_IN_0 = new MessageFormat(STR_SYNTAX_ERROR_IN_0);
        int prev = 0;
        int pos;

        // search for the next instance of $ from the 'prev' position
        while ((pos = textToParse.indexOf('$', prev)) >= 0) {
            // if there was any text before this, add it as a fragment
            // XXX, this check could be modified to go if pos>prev;
            // seems like this current version could stick empty strings
            // into the list
            if (pos > 0) {
                fragments.add(textToParse.substring(prev, pos));
            }

            // if we are at the end of the string, we tack on a $
            // then move past it
            if (pos == (textToParse.length() - 1)) {
                fragments.add("$");
                prev = pos + 1;
            } else if (textToParse.charAt(pos + 1) != '{') {
                // peek ahead to see if the next char is a property or not
                // not a property: insert the char as a literal

                /*
                 * fragments.addElement(value.substring(pos + 1, pos + 2)); prev = pos + 2;
                 */
                if (textToParse.charAt(pos + 1) == '$') {
                    // backwards compatibility two $ map to one mode
                    fragments.add("$");
                    prev = pos + 2;
                } else {
                    // new behaviour: $X maps to $X for all values of X!='$'
                    fragments.add(textToParse.substring(pos, pos + 2));
                    prev = pos + 2;
                }
            } else {
                // property found, extract its name or bail on a typo
                final int endName = textToParse.indexOf('}', pos);
                if (endName < 0) {
                    throw new PropertyParsingException(SYNTAX_ERROR_IN_0.format(new String[] { textToParse }));
                }
                String propertyName = textToParse.substring(pos + 2, endName);
                fragments.add(null);
                propertyRefs.add(propertyName);
                prev = endName + 1;
            }
        }

        // no more $ signs found
        // if there is any tail to the file, append it
        if (prev < textToParse.length()) {
            fragments.add(textToParse.substring(prev));
        }
    }

    static boolean containProperty(final String value) {
        return value.contains(REF_PREFIX);
    }
}
