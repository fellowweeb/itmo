"use strict";


String.prototype.format = function () {
    return [...arguments].reduce((str, arg) => str.replace(/%s/, arg), this);
};


function Const(value) {
    this.value = value;
}

Const.prototype.getValue = function () {
    return this.value;
};
Const.prototype.evaluate = function () {
    return this.value;
};
Const.prototype.toString = function () {
    return this.value.toString();
};
Const.prototype.prefix = function () {
    return this.toString();
};
Const.prototype.postfix = function () {
    return this.toString();
};
Const.prototype.diff = function () {
    return new Const(0);
};
Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.prototype.simplify = function () {
    return Const.ZERO;
};


const variables = {
    'x': 0,
    'y': 1,
    'z': 2
};

function Variable(name) {
    if (variables[name] === undefined)
        throw new Error('Unsupported variable name: %s'.format(name));
    this.name = name;
}

Variable.prototype.evaluate = function (...vars) {
    return vars[variables[this.name]];
};
Variable.prototype.toString = function () {
    return this.name;
};
Variable.prototype.prefix = function () {
    return this.toString();
};
Variable.prototype.postfix = function () {
    return this.toString();
};
Variable.prototype.diff = function (diffName) {
    return diffName === this.name ? Const.ONE : Const.ZERO;
};
Variable.prototype.simplify = function () {
    return new Variable(this.name);
};

Variable.VARS = {};
for (let varName in variables) {
    Variable.VARS[varName] = new Variable(varName);
}

const equalsToConst = (it, value) => it.constructor === Const && it.getValue() === value;


function Operation(...args) {
    if (this.constructor === Operation)
        throw new Error('abstract instance');
    this.args = args;
}

Operation.prototype.evaluate = function (...vars) {
    return this.calc(...(this.args.map(it => it.evaluate(...vars))))
};
Operation.prototype.toString = function () {
    return this.args.map(el => el.toString()).join(' ') + ' ' + this.operatorAsString;
};
Operation.prototype.prefix = function () {
    return '(' + this.operatorAsString + ' ' + this.args.map(el => el.prefix()).join(' ') + ')';
};
Operation.prototype.postfix = function () {
    return '(' + this.args.map(el => el.postfix()).join(' ') + ' ' + this.operatorAsString + ')';
};
Operation.prototype.diff = function (name) {
    return new this.constructor(...(this.args.map(it => it.diff(name))));
};
Operation.prototype.simpleArgs = function () {
    return this.args.map(it => it.simplify());
};
Operation.prototype.fold = function (...simpleArgs) {
    if (!simpleArgs.some(it => it.constructor !== Const)) {
        if (simpleArgs.length === 0)
            return new Const(0);
        return new Const(this.evaluate(...Object.values(variables)));
    }
    return new this.constructor(...simpleArgs);
};

function inheritance(child, parent) {
    child.prototype = Object.create(parent.prototype);
    child.prototype.constructor = child;
}


function Add(...args) {
    Operation.call(this, ...args);
}

inheritance(Add, Operation);
Add.prototype.calc = (...args) => args.reduce((acc, el) => acc + el, 0);
Add.prototype.operatorAsString = '+';
Add.prototype.simplify = function () {
    const simpleArgs = this.simpleArgs().filter(it => !equalsToConst(it, 0));
    if (simpleArgs.length === 1)
        return simpleArgs[0];
    return this.fold(...simpleArgs);
};


function Subtract(left, right) {
    Operation.call(this, left, right);
}

inheritance(Subtract, Operation);
Subtract.prototype.operatorAsString = '-';
Subtract.prototype.calc = (x, y) => x - y;
Subtract.prototype.simplify = function () {
    const simpleLeft = this.args[0].simplify();
    const simpleRight = this.args[1].simplify();
    if (equalsToConst(simpleRight, 0))
        return simpleLeft;
    if (equalsToConst(simpleLeft, 0))
        return new Negate(simpleRight).simplify();
    return this.fold(simpleLeft, simpleRight);
};


function Multiply(...args) {
    this.args = args;
    Operation.call(this, ...args);
}

inheritance(Multiply, Operation);
Multiply.prototype.calc = (...args) => args.reduce((acc, el) => acc * el, 1);
Multiply.prototype.operatorAsString = '*';
Multiply.prototype.diff = function (name) {
    if (this.args.length === 1) {
        return this.args[0].diff(name);
    }
    return new Add(
        new Multiply(this.args[0].diff(name), ...(this.args.slice(1))),
        new Multiply(
            this.args[0],
            new Multiply(...(this.args.slice(1))).diff(name)
        )
    );
};
Multiply.prototype.simplify = function () {
    let simpleArgs = this.simpleArgs();
    for (let el of simpleArgs) {
        if (equalsToConst(el, 0))
            return el;
    }
    simpleArgs = simpleArgs.filter(
        it => !equalsToConst(it, 1)
    );
    if (simpleArgs.length === 0)
        return new Const(1);
    if (simpleArgs.length === 1)
        return simpleArgs[0];
    return this.fold(...simpleArgs);
};


function Divide(left, right) {
    Operation.call(this, left, right);
}

inheritance(Divide, Operation);
Divide.prototype.calc = (x, y) => x / y;
Divide.prototype.operatorAsString = '/';
Divide.prototype.diff = function (name) {
    return new Divide(
        new Subtract(
            new Multiply(this.args[0].diff(name), this.args[1]),
            new Multiply(this.args[0], this.args[1].diff(name))
        ),
        new Multiply(this.args[1], this.args[1])
    );
};
Divide.prototype.simplify = function () {
    const simpleLeft = this.args[0].simplify();
    const simpleRight = this.args[1].simplify();
    if (equalsToConst(simpleLeft, 0))
        return new Const(0);
    if (equalsToConst(simpleRight, 1))
        return new Const(simpleLeft);
    return this.fold(simpleLeft, simpleRight);
};


function Negate(value) {
    this.value = value;
    Operation.call(this, value);
}

inheritance(Negate, Operation);
Negate.prototype.calc = value => -value;
Negate.prototype.operatorAsString = 'negate';
Negate.prototype.simplify = function () {
    const simpleValue = this.value.simplify();
    if (simpleValue.constructor === Negate)
        return this.value.simpleArgs()[0].simplify();
    return this.fold(simpleValue);
};


function Sinh(value) {
    this.value = value;
    Operation.call(this, value);
}

inheritance(Sinh, Operation);
Sinh.prototype.calc = value => Math.sinh(value);
Sinh.prototype.operatorAsString = 'sinh';
Sinh.prototype.simplify = function () {
    const simpleValue = this.value.simplify();
    return this.fold(simpleValue);
};
Sinh.prototype.diff = function (name) {
    return new Multiply(this.args[0].diff(name), new Cosh(this.args[0]));
};


function Cosh(value) {
    this.value = value;
    Operation.call(this, value);
}

inheritance(Cosh, Operation);
Cosh.prototype.calc = value => Math.cosh(value);
Cosh.prototype.operatorAsString = 'cosh';
Cosh.prototype.simplify = function () {
    const simpleValue = this.value.simplify();
    return this.fold(simpleValue);
};
Cosh.prototype.diff = function (name) {
    return new Multiply(this.args[0].diff(name), new Sinh(this.args[0]));
};


function Sum(...args) {
    Add.call(this, ...args);
}

inheritance(Sum, Add);
Sum.prototype.operatorAsString = 'sum';


function Avg(...args) {
    Operation.call(this, ...args);
}

inheritance(Avg, Operation);
Avg.prototype.calc = (...args) => {
    const sumAndCnt = args.reduce((acc, el) => [acc[0] + el, acc[1] + 1], [0, 0]);
    return sumAndCnt[0] / sumAndCnt[1];
};
Avg.prototype.operatorAsString = 'avg';
Avg.prototype.simplify = function () {
    const simpleArgs = this.simpleArgs();
    if (simpleArgs.length === 1)
        return simpleArgs[0];
    return this.fold(...simpleArgs);
};


function parse(str) {
    const arr = str.trim().split(/\s+/);

    function stackUpdate(stack, op, arity) {
        const args = stack.slice(stack.length - arity);
        stack.length -= arity;
        stack.push(new op(...args));
        return stack;
    }

    return arr.reduce((stack, el) => {
        const stUpd2 = op => stackUpdate(stack, op, 2);
        const stUpd1 = op => stackUpdate(stack, op, 1);
        const stUpd0 = op => {
            stack.push(op);
            return stack;
        };
        switch (el) {
            case "+":
                return stUpd2(Add);
            case "-":
                return stUpd2(Subtract);
            case "*":
                return stUpd2(Multiply);
            case "/":
                return stUpd2(Divide);
            case "negate":
                return stUpd1(Negate);
            case "sinh":
                return stUpd1(Sinh);
            case "cosh":
                return stUpd1(Cosh);
            case "x":
            case "y":
            case "z":
                return stUpd0(Variable.VARS[el]);
            default:
                return stUpd0(new Const(parseInt(el)));
        }
    }, []).pop();
}

function ParsingException(message, tokenPosition) {
    Error.call(this);
    this.tokenPosition = tokenPosition;
    if (tokenPosition !== undefined) {
        this.message = message + " at %s token".format(tokenPosition);
    } else {
        this.message = message;
    }
}

Object.setPrototypeOf(ParsingException.prototype, Error.prototype);

function ClosingParenthesisException(closeParenthesis, tokenPosition) {
    ParsingException.call(this, "Expected %s".format(closeParenthesis), tokenPosition);
}

function MissingTokenException(tokenPosition) {
    ParsingException.call(this, "Expected token", tokenPosition);
}

function UnexpectedTokenException(token, openParenthesis, tokenPosition) {
    ParsingException.call(
        this,
        "Expected known variable(%s) or %s, but was %s"
            .format(Object.keys(Variable.VARS).join(','), openParenthesis, token),
        tokenPosition
    );
}

function UnexpectedException(rest, tokenPosition) {
    ParsingException.call(this, "Unexpected %s".format(rest));
}

function MissingOperationException(tokenPosition) {
    ParsingException.call(this, "Expected operation", tokenPosition);
}

function WrongNumberOfArgsException(operatorName, provided, expected, tokenPosition) {
    ParsingException.call(
        this,
        "For %s expected %s args, but provided %s"
            .format(operatorName, expected, provided),
        tokenPosition
    );
}


function parseTokens(str) {
    const splitToArrBy = (token, separator) => {
        let splittedTokens = token.split(separator);
        if (splittedTokens.length > 1) {
            splittedTokens = splittedTokens.flatMap(it => [it, separator]);
            splittedTokens.pop();
        }
        return splittedTokens.filter(it => it.length !== 0);
    };
    const parsedTokens = str.trim().split(/\s+/)
        .flatMap(token => splitToArrBy(token, '('))
        .flatMap(token => splitToArrBy(token, ')'));
    parsedTokens.top = function () {
        return this[this.length - 1];
    };
    const tokensLength = parsedTokens.length;
    parsedTokens.topPosition = function () {
        return tokensLength - this.length + 1;
    };
    return parsedTokens;
}

function parseNotation(inputTokens, order, openParenthesis, closeParenthesis) {
    function parseOp(tokens) {
        if (tokens.length === 0) {
            throw new MissingTokenException(tokens.topPosition());
        }
        const parseArgs = (op, arity) => {
            tokens.pop();
            let args = [];
            for (let i = 0; tokens.top() !== closeParenthesis && (arity === -1 || i < arity); i++) {
                args.push(parseStat(tokens));
            }
            if (arity !== -1 && arity !== args.length) {
                throw new WrongNumberOfArgsException(
                    op.prototype.operatorAsString, args.length, arity, tokens.topPosition()
                );
            }
            return new op(...order(args));
        };
        switch (tokens.top()) {
            case 'sum':
                return parseArgs(Sum, -1);
            case 'avg':
                return parseArgs(Avg, -1);
            case '+':
                return parseArgs(Add, 2);
            case '-':
                return parseArgs(Subtract, 2);
            case '*':
                return parseArgs(Multiply, 2);
            case '/':
                return parseArgs(Divide, 2);
            case 'negate':
                return parseArgs(Negate, 1);
        }
        throw new MissingOperationException(tokens.topPosition());
    }

    function parseStat(tokens) {
        if (tokens.length === 0) {
            throw new MissingTokenException(tokens.topPosition());
        }
        switch (tokens.top()) {
            case openParenthesis:
                tokens.pop();
                const ret = parseOp(tokens);
                if (tokens.top() !== closeParenthesis) {
                    throw new ClosingParenthesisException(closeParenthesis, tokens.topPosition());
                }
                tokens.pop();
                return ret;
            case 'x':
            case 'y':
            case 'z':
                return Variable.VARS[tokens.pop()];
        }
        const number = parseInt(tokens.top());
        if (Number.isNaN(number) || number.toString() !== tokens.top()) {
            throw new UnexpectedTokenException(tokens.top(), openParenthesis, tokens.topPosition());
        }
        tokens.pop();
        return new Const(number);
    }

    const ret = parseStat(inputTokens);
    if (inputTokens.length !== 0) {
        throw new UnexpectedException(inputTokens.join(' '), inputTokens.topPosition());
    }
    return ret;
}

function parsePrefix(str) {
    const inputTokens = parseTokens(str).reverse();
    return parseNotation(inputTokens, it => it, '(', ')');
}

function parsePostfix(str) {
    const inputTokens = parseTokens(str);
    return parseNotation(inputTokens, it => it.reverse(), ')', '(');
}
