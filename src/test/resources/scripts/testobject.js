function TestObject() {
    this.executed = true;
}

TestObject.prototype.isExecuted = function() {
    return this.executed;
}