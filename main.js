var json = {
  states: [
    [
      [0, 0, 1, 1, 0],
      [1, 1, 1, 1, 1],
      [0, 0, 2, 2, 0],
      [1, 0, 2, 2, 1],
      [1, 0, 1, 1, 1],
    ],
    [
      [0, 0, 1, 1, 0],
      [1, 1, 1, 1, 1],
      [0, 0, 2, 2, 0],
      [1, 0, 5, 5, 1],
      [1, 0, 1, 1, 1],
    ],
    [
      [0, 0, 1, 1, 0],
      [1, 1, 2, 2, 1],
      [0, 0, 2, 2, 0],
      [1, 0, 5, 5, 1],
      [1, 0, 1, 1, 1],
    ],
    [
      [0, 0, 1, 1, 0],
      [1, 1, 2, 2, 1],
      [0, 0, 5, 5, 0],
      [1, 0, 5, 5, 1],
      [1, 0, 1, 1, 1],
    ],
    [
      [0, 0, 1, 1, 0],
      [1, 2, 2, 1, 1],
      [0, 0, 5, 5, 0],
      [1, 0, 5, 5, 1],
      [1, 0, 1, 1, 1],
    ],
    [
      [0, 0, 1, 1, 0],
      [2, 2, 1, 1, 1],
      [0, 0, 5, 5, 0],
      [1, 0, 5, 5, 1],
      [1, 0, 1, 1, 1],
    ],
  ],
  countIteration: 15,
  maxO: 6,
  endO: 2,
  endC: 14,
  m: 5,
  n: 5,
};
var counter = json.states.length - 1;
$(document).ready(function () {
  /** Cоздает доску
   */

  var initBoard = () => {
    $(".board").empty();

    var state = json.states[counter];
    var squareVoid = $("<div class='square void'></div>");
    var squareWinPoint = $("<div class='square winPoint'></div>");
    var squareShelf = $("<div class='square shelf'></div>");
    var squareFloor = $("<div class='square floor'></div>");

    var addRow = (rowId) => {
      var row = $("<div class='row'></div>");

      for (var i = 0; i < json.m; i++) {
        var newSquare;
        switch (state[rowId][i]) {
          case 0: {
            newSquare = squareVoid.clone();
            break;
          }
          case 1: {
            newSquare = squareFloor.clone();
            break;
          }
          case 2: {
            newSquare = squareShelf.clone();
            break;
          }
          case 5: {
            newSquare = squareWinPoint.clone();
            break;
          }
        }
        row.append(newSquare);
      }
      $(".board").append(row);
    };

    for (var i = 0; i < json.n; i++) {
      addRow(i);
    }
  };

  let back = () => {
    if (counter !== json.states.length - 1) {
      counter++;
      refresh();
    }
  };

  let move = () => {
    if (counter !== 0) {
      counter--;
      refresh();
    }
  };

  var refresh = () => {
    initBoard();
  };
  $("#move").click(move);
  $("#back").click(back);

  $("#getDepth").click(() => {
    $.ajax("/depth", {
      method: "GET",
      success: function (data) {
        json = data;
        $("#countIteration").text("Число итераций: " + json.countIteration);
        $("#maxO").text("Максимальное число вершин-кандидатов: " + json.maxO);
        $("#endO").text(
          "Число вершин-кандидатов на последней итерации: " + json.endO
        );
        $("#endC").text(
          "Число пройденных вершин на последней итерации: " + json.endC
        );

        counter = json.states.length - 1;
        refresh();
      },
    });
  });

  $("#getWidth").click(() => {
    $.ajax("/wide", {
      method: "GET",
      success: function (data) {
        json = data;
        $("#countIteration").text("Число итераций: " + json.countIteration);
        $("#maxO").text("Максимальное число вершин-кандидатов: " + json.maxO);
        $("#endO").text(
          "Число вершин-кандидатов на последней итерации: " + json.endO
        );
        $("#endC").text(
          "Число пройденных вершин на последней итерации: " + json.endC
        );

        counter = json.states.length - 1;
        refresh();
      },
    });
  });

  refresh();
});

/*
$(document).ready(function () {

  $("#getSolve").click(() => {
    /*
    $.ajax("мыакс", {
      method: "post",
      contentType: "application/json",
      dataType: "json",
      data: JSON.stringify({
        fire: json.fire,
        king: json.king,
        horse: json.horseStartPostiton,
      }),
      success: function (data) {
        json.path = data.path;
        refresh();
      },
    });
  });
  

  var board = [];
  var index = {};

  
  var initBoard = () => {
    board = [];
    tagged = {};
    $(".board").empty();

    

    var square = $("<div class='square'></div>");
    var addRow = (rowId) => {
      var row = $("<div class='row'></div>");
      board[rowId] = [];

      for (var i = 0; i < 8; i++) {
        var identifier = files[i] + rows[rowId];
        var newSquare = square
          .clone()
          .addClass(identifier)
          .append(`<span class='identifier'>${identifier}</span>`)
          .append(`<span class='txt'>-</span>`);
        row.append(newSquare);
        board[rowId][i] = identifier;
        index[identifier] = [rowId, i];
      }
      $(".board").append(row);
    };

    for (var i = 0; i < 8; i++) {
      addRow(i);
    }
    $(".square").on("mousedown", clickHandler);
  };

  /** Добавляет указанную фигуру на доску.
   * @param {*} type тип фигуры.
   * @param {*} square клетка.
   
  var initPiece = (type, square) => {
    console.log(type, square);
    $("." + square).append(`<div class='piece piece-${type}'></div>`);
    $("." + square)
      .find(".txt")
      .html("&nbsp;");
  };

  /** Закрашивает клетку и пишет в нее цифру
   * @param {*} step Клетка
   * @param {*} val Цифра
   
  var tagSquare = (step, val) => {
    if (!$("." + step).hasClass("heat")) {
      $("." + step).addClass("heat");
      $("." + step)
        .find(".txt")
        .text(val);
      let heatVal = val % heatStyleCount;
      $("." + step).addClass("heat-" + heatVal);
    }
  };

  /** Рисует путь
   * @param {*} path массив - путь фигуры.
   
  var renderPath = (path) => {
    let val = 0;
    path.forEach((step) => {
      tagSquare(step, val);
      val++;
    });
  };

  var refresh = () => {
    initBoard();
    renderPath(json.path);
    json.fire.forEach((position) => {
      initPiece(figures.fire, position);
    });

    if (currentPos == 0) {
      initPiece(figures.horse, json.horseStartPostiton);
      initPiece(figures.king, json.king);
    } else {
      json.path.forEach((step, number) => {
        if (number < currentPos) initPiece(figures.fire, step);
        else if (number === currentPos) initPiece(figures.horse, step);
        else if (step === json.king) initPiece(figures.king, step);
      });
    }
  };

  let backStep = () => {
    if (currentPos !== 0) {
      currentPos--;
      refresh();
    }
  };

  let nextStep = () => {
    if (currentPos !== json.path.length - 1) {
      currentPos++;
      refresh();
    }
  };

  function clickHandler(event) {
    var target = $(event.currentTarget);
    var id = target.find(".identifier").text();
    if (event.which == 1) {
      switch (activeFigure) {
        case figures.horse:
          json.horseStartPostiton = id;
          break;
        case figures.king:
          json.king = id;
          break;
        case figures.fire:
          json.fire.push(id);
          break;
      }
      refresh();
    } else {
      json.fire = json.fire.filter(function (elem) {
        return elem != id;
      });
      refresh();
    }
    refresh();
  }
/*
  $("#getSolve").click(() => {
    $.ajax("http://localhost:8080/atilla", {
      method: "post",
      contentType: "application/json",
      dataType: "json",
      data: JSON.stringify({
        fire: json.fire,
        king: json.king,
        horse: json.horseStartPostiton,
      }),
      success: function (data) {
        json.path = data.path;
        refresh();
      },
    });
  });
  

  refresh();
});
*/
