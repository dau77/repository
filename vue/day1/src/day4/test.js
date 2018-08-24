let arr2 = [
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9]
];
let flat = arr2.reduce(function(prev, next) {
    return prev.concat(next);
});
console.log(flat);


/* function a(a) {
    return b + 1;
}
let a = (a) => { return b + 1 }
let a1 = b => b + 1; */
// 去掉function关键字，参数如果有一个，可以省略小括号，小括号和大括号之间有一个箭头，
//如果没有大括号则直接是返回值，有大括号必须写return

function a(b) {
    return function(c) {
        return b + c;
    }
}
// let a = b => c => b+c; // 高阶函数 (>=俩箭头以上)
let a = (b) => {
    return (c) => {
        return b + c;
    }
}
let c = a(1)(2);
console.log('c=' + c);

// 闭包：函数执行的一瞬间叫闭包，(不销毁的作用域)，当执行后返回的结果必须是应用数据类型，被外界变量接收，此时这个函数不会被销毁