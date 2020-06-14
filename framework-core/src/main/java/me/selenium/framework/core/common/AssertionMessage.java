package me.selenium.framework.core.common;

public enum AssertionMessage {
    EQUAL_ERROR_MESSAGE("\n*** ERROR: ''{0}'' Equal Assertion. \nExpected: ''{1}''. \nActual: ''{2}''."),
    NOT_EQUAL_ERROR_MESSAGE("\n*** ERROR: ''{0}'' Not Equal Assertion. \nExpected: ''{1}''. \nActual: ''{2}''."),
    LIST_EQUAL_ERROR_MESSAGE("\n*** ERROR: ''{0}'' List Equal Assertion.\nExpected: ''{1}''.\nGot     : ''{2}''.\nMissing elements: ''{3}''.\nExtra elements  : ''{4}''"),
    LIST_CONTAIN_ERROR_MESSAGE("\n*** ERROR: ''{0}'' List Contain Assertion.\nExpected:\n {1}.\nGot:\n {2}.\nMissing elements: {3}"),
    LIST_NOT_CONTAIN_ERROR_MESSAGE("*** ERROR: ''{0}'' List Not Contain Assertion.\nExpected:\n {1}.\nGot:\n {2}.\nCommon elements: {3}"),
    LIST_NOT_SORTED("*** ERROR: ''{0}'' List Not Sorted Assertion.\nExpected order: {1}.\nGot:\n {2}.");

    private String value;

    private AssertionMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
