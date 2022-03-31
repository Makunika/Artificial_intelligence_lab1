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
    let width = 900 / state.length;
    var squareVoid = $(`<div class='square void' style='width: ${width}px; height: ${width}px;'></div>`);
    var squareWinPoint = $(`<div class='square winPoint' style='width: ${width}px; height: ${width}px;'></div>`);
    var squareShelf = $(`<div class='square shelf' style='width: ${width}px; height: ${width}px;'></div>`);
    var squareFloor = $(`<div class='square floor' style='width: ${width}px; height: ${width}px;'></div>`);

    var addRow = (rowId) => {
      var row = $(`<div class='row' style='height: ${width}px;'></div>`);

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
        $("#countStep").text(
            "Число шагов: " + (json.states.length - 1)
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
        $("#countStep").text(
            "Число шагов: " + (json.states.length - 1)
        );


        counter = json.states.length - 1;
        refresh();
      },
    });
  });

  $("#getA").click(() => {
    $.ajax("/a", {
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
        $("#countStep").text(
            "Число шагов: " + (json.states.length - 1)
        );

        counter = json.states.length - 1;
        refresh();
      },
    });
  });

  $("#getSMA").click(() => {
    $.ajax("/sma", {
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
        $("#countStep").text(
            "Число шагов: " + (json.states.length - 1)
        );

        counter = json.states.length - 1;
        refresh();
      },
    });
  });

  refresh();
});



hui = (n, d) => {
  let srt = '';
  for (let i = 2; i < d + 1; i++) {
    srt+=`x^(${i})+`
  }

  return `${n+1}=1+x+`+srt;

}
