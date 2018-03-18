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
	intersect {k1,m1} {k2,m2}
*)
LinesIntersection := Function[{l1, l2},
	Module[{xc,yc,k1,k2,m1,m2},
		k1 = l1[[1]];
		m1 = l1[[2]];
		k2 = l2[[1]];
		m2 = l2[[2]];
		xc = -(m1-m2)/(k1-k2);
		yc = k1*xc + m1;
		Print["x=", xc];
		Print["y=", yc];
		Return[{xc,yc}];
	];
];
(*LinesIntersection[{1,0},{-1,1}]*)

(*
	{x0, k, m}
*)
l0 = {0, 1, 0};
l1 = {1,-0.5, 1.5};
l2 = {2, 2, -3.5};
l3 = {3, 0, 0};

u0 = {0, -1, 2};
u1 = {1,-0.5, 2.5};
u2 = {2, 2, -1.5};
u3 = {3, 0, 0};

lowerNodes = {l0,l1,l2,l3};
upperNodes = {u0,u1,u2,u3};

lowerSegments = NodesToConditionalSegments[lowerNodes]
upperSegments = NodesToConditionalSegments[upperNodes]

l[x_] := Piecewise[lowerSegments];
u[x_] := Piecewise[upperSegments];

Plot[{l[x], u[x], Sin[x]}, {x,0,2*Pi}]

Func = Function[{sp, spozn, y, yd}, 
	If[Length[sp] != Length[spozn] + 1, Throw["sp.length != spozn.length + 1"]];
	Module[{lower, upper, x2, x1, l1, l2, p},
		lower = {};
		upper = {};
		For[i=1, i<=Length[spozn], i++,
			x1 = sp[[i]];
			x2 = sp[[i+1]];
			line1 = {yd[x1], y[x1]-yd[x1]*x1};
			line2 = {yd[x2], y[x2]-yd[x2]*x2};
			
			
			p = LinesIntersection[line1,line2];
			lower = Append[lower, {l3}];
			upper = Append[upper, {u1}];
			
		];
		Print["in func lower = ", lower];
		Return[{lower, upper}];
	];
];

res = Func[{0, Pi, 2Pi}, {1, -1}, Sin, Cos];
Print["res = ", res];
lowerNodes = res[[1]];
upperNodes = res[[2]];

Print["lower nodes = ", lowerNodes];

lowerSegments = NodesToConditionalSegments[lowerNodes]
upperSegments = NodesToConditionalSegments[upperNodes]

l[x_] := Piecewise[lowerSegments];
u[x_] := Piecewise[upperSegments];

Plot[{l[x], u[x], Sin[x]}, {x,0,2*Pi}]








