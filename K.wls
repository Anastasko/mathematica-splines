#!/usr/bin/env wolframscript
(* ::Package:: *)

ClearAll["Global`*"]
MyLinearFunction[k_, m_][x_] := k*x+m
(*
	{x0, k, m}
*)
p0 = {0, 1, 1};
p1 = {1,-0.5, 3};
p2 = {2, 2, -2};
p3 = {3, 0, 0};
arr = {p0,p1,p2,p3};
Print[arr]
conditionalSegments = {};
For[i=1, i<=Length[arr], i++,
	x0 = arr[[i]][[1]];
	k = arr[[i]][[2]];
	m = arr[[i]][[3]];
	Print[k];
	thisLine[x_] := MyLinearFunction[k,m-x0][x];
	conditionalSegments = Append[conditionalSegments, {thisLine[x], x<x0}]
]
Print[conditionalSegments]

l[x_] := Piecewise[conditionalSegments]

Plot[l[x], {x,0,3}]



