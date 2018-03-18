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
		xc = -(m1-m2)/(k1-k2);
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

(*
	{x0, k, m}
*)(*
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

*)

Func = Function[{sp, spozn, y, yd}, 
	If[Length[sp] != Length[spozn] + 1, Throw["sp.length != spozn.length + 1"]];
	lower = {};
	upper = {};
	For[i=1, i<=Length[spozn], i++,
		x1bb = sp[[i]];
		x1 = x1bb;
		x2 = sp[[i+1]];
		k1 = yd[x1];
		k2 = yd[x2];
		m1 = y[x1]-k1*x1;
		m2 = y[x2]-k2*x2;
						
			pA = {x1, y[x1]};
			pB = {x2, y[x2]};
			pC = LinesIntersection[{k1,m1},{k2,m2}];
			xm = pC[[1]];
			pD = {xm, y[xm]};
			lineAC = MakeLine[pA, pC];
			lineAD = MakeLine[pA, pD];
			lineBC = MakeLine[pB, pC];
			lineBD = MakeLine[pB, pD];
			
			ozn = spozn[[i]];
			
			If[ozn > 0,
				AppendTo[lower, {x1bb, lineAD[[1]], lineAD[[2]]}];
				AppendTo[upper, {x1bb, lineAC[[1]], lineAC[[2]]}];
				AppendTo[lower, {xm, lineBD[[1]], lineBD[[2]]}];
				AppendTo[upper, {xm, lineBC[[1]], lineBC[[2]]}];
			];
			
			If[ozn < 0,
				AppendTo[upper, {x1bb, lineAD[[1]], lineAD[[2]]}];
				AppendTo[lower, {x1bb, lineAC[[1]], lineAC[[2]]}];
				AppendTo[upper, {xm, lineBD[[1]], lineBD[[2]]}];
				AppendTo[lower, {xm, lineBC[[1]], lineBC[[2]]}];
			];
		
	];
	AppendTo[lower, {Last[sp], 0, 0}];
	AppendTo[upper, {Last[sp], 0, 0}];
	{lower, upper}
];

MakeDemo = Function[{sp, spozn, y, yd},

	res = Func[sp, spozn, y, yd];

	lowerNodes = res[[1]];
	upperNodes = res[[2]];

	Print["lower nodes = ", lowerNodes];
	Print["upper nodes = ", upperNodes];

	lowerSegments = NodesToConditionalSegments[lowerNodes];
	upperSegments = NodesToConditionalSegments[upperNodes];

	l[x_] := Piecewise[lowerSegments];
	u[x_] := Piecewise[upperSegments];
];

MakeDemo[{0, Pi, 2*Pi}, {1, -1}, Sin, Cos];
Plot[{l[x], u[x], Sin[x]}, {x, 0, 2*Pi}]










