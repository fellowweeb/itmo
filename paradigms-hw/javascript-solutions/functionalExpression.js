"use strict";


const cnst = str => {
    switch (str) {
        case 'pi':
            return () => Math.PI;
        case 'e':
            return () => Math.E;
        default:
            return () => parseInt(str, 10);
    }
};
const pi = cnst('pi');
const e = cnst('e');

const variable = str => (...vars) => {
    const map = {'x': 0, 'y': 1, 'z': 2};
    return vars[map[str]];
};
const variables = {
    'x': variable('x'),
    'y': variable('y'),
    'z': variable('z')
};

const naryOperator = calc => (...args) => (...vars) => calc(...(args.map(it => it(...vars))));
const add = naryOperator((x, y) => x + y);
const subtract = naryOperator((x, y) => x - y);
const multiply = naryOperator((x, y) => x * y);
const divide = naryOperator((x, y) => x / y);
const negate = naryOperator(x => -x);
const sin = naryOperator(Math.sin);
const cos = naryOperator(Math.cos);


function parse(str) {
    let arr = str.trim().split(/\s+/);

    function stackUpdate(stack, op, arity) {
        const args = stack.slice(stack.length - arity);
        stack.length -= arity;
        stack.push(op(...args));
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
                return stUpd2(add);
            case "-":
                return stUpd2(subtract);
            case "*":
                return stUpd2(multiply);
            case "/":
                return stUpd2(divide);
            case "sin":
                return stUpd1(sin);
            case "cos":
                return stUpd1(cos);
            case "negate":
                return stUpd1(negate);
            case "x":
            case "y":
            case "z":
                return stUpd0(variables[el]);
            case "pi":
                return stUpd0(pi);
            case "e":
                return stUpd0(e);
            default:
                return stUpd0(cnst(el));
        }
    }, []).pop();
}
