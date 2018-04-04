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
	Print[conditionalSegments];
    conditionalSegments
]

(*
	intersect {k1,m1} {k2,m2}
*)
LinesIntersection := Function[{l1, l2},

		k1 = l1[[1]];
		m1 = l1[[2]];
		k2 = l2[[1]];
		m2 = l2[[2]];
		If[k1 == k2 && m1 != m2, Throw["Lines Intersection: k1 == k2 && m1 != m2"]];
		xc = -(m1-m2);
		If[xc != 0, xc = xc/(k1-k2)];
		yc = k1*xc + m1;
		{xc,yc}

];
(*LinesIntersection[{1,0},{-1,1}]*)

(*
	make line {x1,y1} {x2,y2}
*)
MakeLine := Function[{p1, p2},

		x1 = p1[[1]];
		y1 = p1[[2]];
		x2 = p2[[1]];
		y2 = p2[[2]];
		k = (y2-y1)/(x2-x1);
		m = y1-k*x1;
		{k,m}

];
(*MakeLine[{1,1},{2,4}]*)

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
	Plot[{lo[x], up[x], y[x]}, {x, le, ri}, Epilog -> {Red, PointSize@Medium, Point[pointDots]}]
];

MakeDemo := Function[{name, yfunc, sp},

	lol = Import["data/" <> name <> "-lower.txt", "Table"];
	upu = Import["data/" <> name <> "-upper.txt", "Table"];

	res = {lol, upu};
	MyPrint[res];
	Print[Text[Style[yfunc, 16]]];
	MyVisualize[res, yfunc, First[sp], Last[sp]]
];

(*
*)
MyPrint := Function[arg,
	Print["lower: ", arg[[1]]];
	Print["upper: ", arg[[2]]];
];

points1 = {-Pi, 0, Pi, 2*Pi};
ozn1 = {1, -1, 1};
func1 := Function[x, -Sin[x]];
func1d := Function[x, -Cos[x]];

points2 = {-Pi, 0, 2*Pi};
ozn2 = {1,-1};
func2 := Function[x, x*x*x];
func2d := Function[x, 3*x*x];

MakeDemo["-sin", func1, points1]
MakeDemo["x3", func2, points2]
MakeDemo["x^3 div 20 - 5sin", Function[x, x*x*x/20 - 5*Sin[x]], points1]
MakeDemo["x^3 times -sin", Function[x, -x*x*x*Sin[x]], points1]

Plot[(x*x*x/20 - 5*Sin[x]), {x, -Pi, 2*Pi}]
(*
Plot[(x^2 - 8*x +7)*Sin[2*x-1] + Cos[5*x+1]*2^(2x-3), {x, -2,5}]
*)


(* ::InheritFromParent:: *)
(**)


(* ::Message:: *)
(*Part::partw*)


(* ::InheritFromParent:: *)
(**)


(* ::InheritFromParent:: *)
(**)


(* ::InheritFromParent:: *)
(**)


(* ::InheritFromParent:: *)
(**)
