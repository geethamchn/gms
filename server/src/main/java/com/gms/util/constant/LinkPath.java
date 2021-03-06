package com.gms.util.constant;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class LinkPath {

    private LinkPath() {}

    private static final String LINK = "_links";
    private static final String HREF = "href";
    private static final String SELF = "self";

    public static final String EMBEDDED = "_embedded.";
    public static final String PAGE_SIZE_PARAM_META = "Name of the URL query string parameter that indicates how many results to return at once.";
    public static final String PAGE_SORT_PARAM_META = "Properties that should be sorted by in the format property,property(,ASC|DESC). Default sort direction is ascending. Use multiple sort parameters if you want to switch directions, e.g. ?sort=property1&sort=property2,asc";
    public static final String PAGE_PAGE_PARAM_META = "Name of the URL query string parameter that indicates what page to return.";

    /**
     * Returns a string with the format <code>"_links."</code> + <code>what</code> param + <code>".href"</code>.
     * @param what The desired String to be included in the resulting string.
     * @return A string concatenation of the three mentioned string
     */
    public static String get(String what) {
        return String.format("%s.%s.%s", LINK, what, HREF);
    }

    /**
     * Returns a string with the format "_links.self." + <code>what</code> param .
     * @param what The desired String to be included in the resulting string.
     * @return A string concatenation of the three mentioned string
     */
    public static String getSelf(String what) {
        return String.format("%s.%s.%s", LINK, SELF, what);
    }

    /**
     * Returns a string with the format "_links.self." + <code>what</code> param . This is a shortcut for the
     * {@link LinkPath#get(String)} method providing {@link LinkPath#SELF} as argument.
     * @return A string concatenation of the three mentioned string
     */
    public static String get() {
        return get(SELF);
    }

}
