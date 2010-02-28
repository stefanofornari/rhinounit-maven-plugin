function testsuite2() {
}

testsuite2.prototype.testSourceScriptExecution = function testSourceScriptExecution() {
    var obj = new TestObject();

    assertTrue(obj.isExecuted());
}
