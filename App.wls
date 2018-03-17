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
		thisLine[xx_] := LinearFunction[k,m][xx];
		conditionalSegments = Append[conditionalSegments, {thisLine[x], x<nextx0}];
	];
    conditionalSegments
]

(*
	{x0, k, m}
*)
p0 = {0, 1, 0};
p1 = {1,-0.5, 1.5};
p2 = {2, 2, -3.5};
p3 = {3, 0, 0};
nodes = {p0,p1,p2,p3};
Print[nodes];
conditionalSegments = NodesToConditionalSegments[nodes]

l[x_] := Piecewise[conditionalSegments]
u[x_] := 1
Plot[{l[x], u[x]}, {x,0,3}]






