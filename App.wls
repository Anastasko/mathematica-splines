#!/usr/bin/env wolframscript
(* ::Package:: *)

ClearAll["Global`*"]

(*
  {{x0, k, m},...} => {{1.5-0.5*x,x<2}, ...}
 *)
NodesToConditionalSegments := Function[nodes,
	conditionalSegments = {};
	For[i=1, i<=Length[nodes]-1, i++,
		x0 = nodes[[i]][[1]];
		k = nodes[[i]][[2]];
		m = nodes[[i]][[3]];
		nextx0 = nodes[[i+1]][[1]];
		AppendTo[conditionalSegments, {k*x+m, x<nextx0}];
	];
    conditionalSegments
]

NodesToConditionalSegments2 := Function[nodes,
	conditionalSegments = {};
	For[i=1, i<=Length[nodes]-1, i++,
		x0 = nodes[[i]][[1]];
		a = nodes[[i]][[2]];
		b = nodes[[i]][[3]];
		c = nodes[[i]][[4]];
		nextx0 = nodes[[i+1]][[1]];
		AppendTo[conditionalSegments, {a*x*x+b*x+c, x<nextx0}];
	];
    conditionalSegments
]

MyVisualize := Function[{res, y, le, ri},

	lowerNodes = res[[1]];
	upperNodes = res[[2]];
	
	lowerSegments = NodesToConditionalSegments[lowerNodes];
	upperSegments = NodesToConditionalSegments[upperNodes];

	lo[x_] := Piecewise[lowerSegments];
	up[x_] := Piecewise[upperSegments];
	pointDots = {};
	For[lu=1, lu<=2, ++lu,
	For[i=1,i<=Length[res[[lu]]],++i,
		px = res[[lu]][[i]][[1]];
		k = res[[lu]][[i]][[2]];
		m = res[[lu]][[i]][[3]];
		AppendTo[pointDots, {px, k*px+m}];
	];
	];
	Plot[{lo[x], up[x], y[x]}, {x, le, ri}, 
	Epilog -> {Red, PointSize@Medium, Point[pointDots]}
	]
];

MyVisualize2 := Function[{res, y, le, ri},

	lowerNodes = res[[1]];
	upperNodes = res[[2]];
	
	lowerSegments = NodesToConditionalSegments2[lowerNodes];
	upperSegments = NodesToConditionalSegments2[upperNodes];
	
	lo[x_] := Piecewise[lowerSegments];
	up[x_] := Piecewise[upperSegments];
	pointDots = {};
	For[lu=1, lu<=2, ++lu,
	For[i=1,i<=Length[res[[lu]]],++i,
		px = res[[lu]][[i]][[1]];
		a = res[[lu]][[i]][[2]];
		b = res[[lu]][[i]][[3]];
		c = res[[lu]][[i]][[4]];
		AppendTo[pointDots, {px, a*px*px+b*px+c}];
	];
	];
	Plot[{lo[x], up[x], y[x]}, {x, le, ri}, 
	Epilog -> {Red, PointSize@Medium, Point[pointDots]}
	]
];

MakeDemo := Function[{show, name, yfunc},
	If[show == 1,
	    points = Import["data/" <> name <> "-bounds.txt", "Table"][[1]];
		lol = Import["data/" <> name <> "-lower.txt", "Table"];
		upu = Import["data/" <> name <> "-upper.txt", "Table"];
		res = {lol, upu};
		MyPrint[res];
		Print[Text[Style[yfunc, 16]]];
		MyVisualize[res, yfunc, First[points], Last[points]]
	]
];

MakeDemo2 := Function[{show, name, yfunc},
	If[show == 1,
	    points = Import["data/" <> name <> "-bounds.txt", "Table"][[1]];
		lol = Import["data/" <> name <> "-lower.txt", "Table"];
		upu = Import["data/" <> name <> "-upper.txt", "Table"];
		res = {lol, upu};
		MyPrint[res];
		Print[Text[Style[yfunc, 16]]];
		MyVisualize2[res, yfunc, First[points], Last[points]]
	]
];

(*
*)
MyPrint := Function[arg,
	Print["lower: ", arg[[1]]];
	Print["upper: ", arg[[2]]];
];

ff[x_] = (x^2 - 8*x +7)*Sin[2*x-1] + Cos[5*x+1]*2^(2x-3);

MakeDemo[1, "x^2 div 2 sin inc 2", Function[x, x*x/2/(Sin[x] + 1.2)]]
MakeDemo[0, "sin div (cos + 1.2)", Function[x, Sin[x]/(Cos[x] + 1.2)]]
MakeDemo2[1, "(x^2-2) cos - 2x sin", Function[x, (x^2-2)*Cos[x] - 2x*Sin[x]]]
MakeDemo[1, "x^2 times -sin", Function[x, -x*x*Sin[x]]]
MakeDemo[0, "x^2 div 10 on sin", Function[x, x*x/10]]
MakeDemo2[1, "x^3 div 30 - 5cos", Function[x, x*x*x/30 - 5*Cos[x]]]
MakeDemo[1, "x^2 div 10 + 5sin", Function[x, x*x/10 + 5*Sin[x]]]
MakeDemo2[1, "x^3 div 30 + 5cos", Function[x, x*x*x/30 + 5*Cos[x]]]
MakeDemo[1, "x^2 div 10 - 5sin", Function[x, x*x/10 - 5*Sin[x]]]
MakeDemo2[1, "cos-splines", Function[x, Cos[x]]]
MakeDemo[1, "-sin", Function[x, -Sin[x]]]
MakeDemo2[1, "x3-splines", Function[x, x*x*x]]
MakeDemo[1, "x3d", Function[x, 3*x*x]]




