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
		conditionalSegments = Append[conditionalSegments, {thisLine[x], x<nextx0}];
	];
    conditionalSegments
]

(*
	{x0, k, m}
*)
l0 = {0, 1, 0,};
l1 = {1,-0.5, 1.5};
l2 = {2, 2, -3.5};
l3 = {3, 0, 0};

lowerNodes = {l0,l1,l2,l3};

u0 = {0, -1, 2};
u1 = {1,-0.5, 2.5};
u2 = {2, 2, -1.5};
u3 = {3, 0, 0};

upperNodes = {u0,u1,u2,u3};

lowerSegments = NodesToConditionalSegments[lowerNodes]
upperSegments = NodesToConditionalSegments[upperNodes]

l[x_] := Piecewise[lowerSegments];
u[x_] := Piecewise[upperSegments];

Plot[{l[x], u[x], Sin[x]}, {x,0,2*Pi}]

Func = Function[{sp, spozn, y, yd}, 
	If[Length[sp] != Length[spozn] + 1, Throw["sp.length != spozn.length + 1"]];
	
];

Func[{0, Pi, 2Pi}, {1, -1}, Sin[x], Cos[x]]







