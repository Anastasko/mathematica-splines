#!/usr/bin/env wolframscript
(* ::Package:: *)

ClearAll["Global`*"]

(*
  LinearFunction[k,m] returns a linear function k*x+m
 *)
LinearFunction [k_, m_][x_] := k*x+m

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
		thisLine[xx_] := LinearFunction[k, m][xx];
		AppendTo[conditionalSegments, {thisLine[x], x<nextx0}];
	];
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
(*
	Print["lower nodes = ", lowerNodes];
	Print["upper nodes = ", upperNodes];
*)
	lowerSegments = NodesToConditionalSegments[lowerNodes];
	upperSegments = NodesToConditionalSegments[upperNodes];

	lo[x_] := Piecewise[lowerSegments];
	up[x_] := Piecewise[upperSegments];
	pointDots = {};
	For[lu=1, lu<=2, ++lu,
	For[i=1,i<=Length[lowerNodes],++i,
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
	Print[Text[Style[yfunc, 16]]];
	MyVisualize[res, yfunc, First[sp], Last[sp]]
];

(*
*)
MyPrint := Function[{title, arg},
	Print[title];
	Print["lower: ", arg[[1]]];
	Print["upper: ", arg[[2]]];
];

AddZeroes := Function[arg,
	arrayWithZeroes = {};
	n = Length[arg];
	For[i=1, i<=n-1, ++i,
		AppendTo[arrayWithZeroes, arg[[i]]];
		x1 = arg[[i]][[1]];
		k = arg[[i]][[2]];
		m = arg[[i]][[3]];
		x2 = arg[[i+1]][[1]];
		If[(k*x1+m)*(k*x2+m)<0,
			p = LinesIntersection[{0,0}, {k, m}];
			AppendTo[arrayWithZeroes, {p[[1]], k, m}];
		];
	];
	AppendTo[arrayWithZeroes, arg[[Length[arg]]]];
	arrayWithZeroes
];

MyMultFunc := Function[{farg, garg},
	temp1 = Func1[{AddZeroes[farg[[1]]], AddZeroes[farg[[2]]]}];
	temp2 = Func1[{AddZeroes[garg[[1]]], AddZeroes[garg[[2]]]}];
	aa = Func2[temp1, temp2];
	f = aa[[1]];
	g = aa[[2]];
	MyCheck[Length[f[[1]]], Length[f[[2]]], "f.lower.len != f.upper.len"];
	MyCheck[Length[g[[1]]], Length[g[[2]]], "g.lower.len != g.upper.len"];
	Print[Length[g[[1]]]];
	MyCheck[Length[f[[1]]], Length[g[[1]]], "@MyMult, Length[f[[1]]] != Length[g[[1]]]"];
	n = Length[f[[1]]];
	Print[n];
	resUpper = {};
	resLower = {};
	For[i=1, i<n, ++i,
        fil = f[[1]][[i]];
	    fiu = f[[2]][[i]];
	    gil = g[[1]][[i]];
	    giu = g[[2]][[i]];
	    glk = gil[[2]];
	    glm = gil[[3]];
	    guk = giu[[2]];
	    gum = giu[[3]];
	    flk = fil[[2]];
	    flm = fil[[3]];
	    fuk = fiu[[2]];
	    fum = fiu[[3]];
	    x1 = fil[[1]];
	    x2 = f[[1]][[i+1]][[1]];
	    gLowerMin = Min[glk*x1+glm, glk*x2+glm];
	    fLowerMin = Min[flk*x1+flm, flk*x2+flm];
	    gUpperMax = Max[guk*x1+gum, guk*x2+gum];
	    fUpperMax = Max[fuk*x1+fum, fuk*x2+fum];

	    If[0 <= fLowerMin && 0 <= gLowerMin,
	       Print["go 11"];
	       resLower = Join[resLower, MyCombine[fil, gil, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fiu, giu, x1, x2, 2]];
	   ];
	    If[fUpperMax <= 0 && 0 <= gLowerMin,
	       Print["go 21"];
	       resLower = Join[resLower, MyCombine[fil, giu, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fiu, gil, x1, x2, 2]];
	   ];
	    If[(fUpperMax > 0 && 0 > fLowerMin) && 0 <= gLowerMin,
	       Print["go 31"];
	       resLower = Join[resLower, MyCombine[fil, giu, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fiu, giu, x1, x2, 2]];
	   ];

	   If[0 <= fLowerMin && 0 >= gUpperMax,
	       Print["go 12"];
	       resLower = Join[resLower, MyCombine[fiu, gil, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fil, giu, x1, x2, 2]];
	   ];
	   If[fUpperMax <= 0 && 0 >= gUpperMax,
	       Print["go 22"];
	       resLower = Join[resLower, MyCombine[fiu, giu, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fil, gil, x1, x2, 2]];
	   ];
	   If[(fUpperMax > 0 && 0 > fLowerMin) && 0 >= gUpperMax,
	       Print["go 32"];
	       resLower = Join[resLower, MyCombine[fiu, gil, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fil, gil, x1, x2, 2]];
	   ];

	   If[0 <= fLowerMin && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["go 13"];
	       resLower = Join[resLower, MyCombine[fiu, gil, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fiu, giu, x1, x2, 2]];
	   ];
	    If[fUpperMax <= 0 && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["go 23"];
	       resLower = Join[resLower, MyCombine[fil, giu, x1, x2, 1]];
	       resUpper = Join[resUpper, MyCombine[fil, gil, x1, x2, 2]];
	   ];
	   If[(fUpperMax > 0 && 0 > fLowerMin) && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["go 33"];
	       resLower = Join[resLower, MyCombine2[fil, fiu, gil, giu]];
	       resUpper = Join[resUpper, MyCombine2[fil, fiu, gil, giu]];
	   ];
	];
	AppendTo[resLower, {Last[f[[1]]][[1]], 0, 0}];
	AppendTo[resUpper, {Last[f[[1]]][[1]], 0, 0}];
	Print[resLower];
	{ resLower, resUpper }
];

MyCombine := Function[{f, g, x1, x2, loup},
	MyCheck[Length[f], 3, "f.length != 3"];
	MyCheck[Length[g], 3, "g.length != 3"];
	myCombineRes = Null;
	k1c = f[[2]];
	k2c = g[[2]];
	m1c = f[[3]];
	m2c = g[[3]];
	If[myCombineRes == Null && k1c == 0, myCombineRes = {{x1, k2c*m1c, m2c*m1c}}];
	If[myCombineRes == Null && k2c == 0, myCombineRes = {{x1, k1c*m2c, m1c*m2c}}];
	If[myCombineRes == Null,
	    xxx = Func[
	       {x1, x2},
	       {k1c*k2c},
	       Function[x, k1c*k2c*x*x + x*(k2c*m1c + k1c*m2c) + m1c*m2c],
	       Function[x, 2*k1c*k2c*x + k2c*m1c + k1c*m2c]
	       ];
	     myCombineRes = xxx[[loup]];
	];
	myCombineRes
];

points1 = {-Pi, 0, Pi, 2*Pi};
ozn1 = {1, -1, 1};
func1 := Function[x, -Sin[x]];
func1d := Function[x, -Cos[x]];

points2 = {-Pi, 0, 2*Pi};
ozn2 = {1,-1};
func2 := Function[x, x*x*x];
func2d := Function[x, 3*x*x];

plusFunc := Function[{a,b}, a+b];
minusFunc := Function[{a,b}, a-b];
multFunc := Function[{a,b}, a*b];

(*
mult = MyMultFunc[res1, res2];
MyVisualize[mult, Function[x, multFunc[func1[x], func2[x]]], -Pi, 2*Pi]
*)

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


(* ::InheritFromParent:: *)
(**)


(* ::InheritFromParent:: *)
(**)
