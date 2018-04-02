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

Func := Function[{sp, spozn, y, yd}, 
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
	AppendTo[lower, {Last[sp], lower[[Length[lower]]][[2]], lower[[Length[lower]]][[3]]}];
	AppendTo[upper, {Last[sp], upper[[Length[upper]]][[2]], upper[[Length[upper]]][[3]]}];
	{lower, upper}
];

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

MakeDemo := Function[{sp, spozn, yfunc, yd},

	res = Func[sp, spozn, yfunc, yd];

	MyVisualize[res, yfunc, First[sp], Last[sp]]
];

(*
*)
MyCheck := Function[{arg1, arg2, msg},
	text = StringJoin["arg1.", msg, " ", ToString[arg1], " != arg2.", msg, " ", ToString[arg2], " "];
	If[arg1 != arg2, Throw[text]];
];

(*
arg1,2 = { 
		{ {1, k, m}, {2, k, m} } - lower , 
		{ {1, k, m}, {2, k, m} } - upper
	   }
	   \:043d\:0430 \:0432\:0445\:0456\:0434 \:0434\:0432\:0430 \:0444\:0443\:043d\:043a\:0446\:0456\:043e\:043d\:0430\:043b\:044c\:043d\:0438\:0445 \:0440\:043e\:0437\:0448\:0438\:0440\:0435\:043d\:043d\:044f
	   \:043d\:0430 \:0432\:0438\:0445\:0456\:0434 \:0442\:0456 \:0436 2 \:0440\:043e\:0437\:0448\:0438\:0440\:0435\:043d\:043d\:044f \:0437 \:043a\:043e\:043c\:0431\:0456\:043d\:043e\:0432\:0430\:043d\:0438\:043c\:0438 \:0442\:043e\:0447\:043a\:0430\:043c\:0438 x
*)\.10
Func2 := Function[{arg1, arg2},
	MyCheck[Length[arg1[[1]]], Length[arg1[[2]]], "arg1 lower and upper length must be equal"];
	MyCheck[Length[arg2[[1]]], Length[arg2[[2]]], "arg2 lower and upper length must be equal"];
	n = Length[arg1[[1]]];
	m = Length[arg2[[1]]];
	MyCheck[arg1[[1]][[1]][[1]], arg2[[1]][[1]][[1]], "lower.start"];
	MyCheck[arg1[[2]][[1]][[1]], arg2[[2]][[1]][[1]], "upper.start"];
	MyCheck[arg1[[1]][[n]][[1]], arg2[[1]][[m]][[1]], "lower.end"];
	MyCheck[arg1[[2]][[n]][[1]], arg2[[2]][[m]][[1]], "upper.end"];
	res1 = {};
	res2 = {};
	For[lu=1, lu<=2, ++lu,
		mas1 = {};
		mas2 = {};
		i = n;
		j = m;
		While[i>0 || j>0,
			po1 = arg1[[lu]][[i]];
			po2 = arg2[[lu]][[j]];
			If[po1[[1]] == po2[[1]],
				AppendTo[mas1, po1];
				AppendTo[mas2, po2];
				--i;
				--j;
			];
			If[po1[[1]] > po2[[1]],
				AppendTo[mas1, po1];
				AppendTo[mas2, {po1[[1]], po2[[2]], po2[[3]]}];
				--i;
			];
			If[po2[[1]] > po1[[1]],
				AppendTo[mas1, {po2[[1]], po1[[2]], po1[[3]]}];
				AppendTo[mas2, po2];
				--j;
			];
		];
		AppendTo[res1, Reverse[mas1]];
		AppendTo[res2, Reverse[mas2]];
	];
	{ res1, res2 }
];

(*
arg = { 
		{ {1, k, m}, {2, k, m} } - lower , 
		{ {1, k, m}, {2, k, m} } - upper
	   }
	   \:043d\:0430 \:0432\:0445\:0456\:0434 \:043e\:0434\:043d\:0435 \:0444\:0443\:043d\:043a\:0446\:0456\:043e\:043d\:0430\:043b\:044c\:043d\:0435 \:0440\:043e\:0437\:0448\:0438\:0440\:0435\:043d\:043d\:044f
	   \:043d\:0430 \:0432\:0438\:0445\:0456\:0434 \:0442\:0435 \:0436 \:0440\:043e\:0437\:0448\:0438\:0440\:0435\:043d\:043d\:044f \:0437 \:043a\:043e\:043c\:0431\:0456\:043d\:043e\:0432\:0430\:043d\:0438\:043c\:0438 \:0442\:043e\:0447\:043a\:0430\:043c\:0438 x
*)\.10
Func1 := Function[arg,
	n = Length[arg[[1]]];
	m = Length[arg[[2]]];
	
		mas1 = {};
		mas2 = {};
		i = n;
		j = m;
		While[i>0 || j>0,
			po1 = arg[[1]][[i]];
			po2 = arg[[2]][[j]];
			If[po1[[1]] == po2[[1]],
				AppendTo[mas1, po1];
				AppendTo[mas2, po2];
				--i;
				--j;
			];
			If[po1[[1]] > po2[[1]],
				AppendTo[mas1, po1];
				AppendTo[mas2, {po1[[1]], po2[[2]], po2[[3]]}];
				--i;
			];
			If[po2[[1]] > po1[[1]],
				AppendTo[mas1, {po2[[1]], po1[[2]], po1[[3]]}];
				AppendTo[mas2, po2];
				--j;
			];
		];
	
	{ Reverse[mas1], Reverse[mas2] }
];

(*
BiFunc
*)
MyBiFunc := Function[{arg1, arg2, func}, 
	MyCheck[Length[arg1[[1]]], Length[arg1[[2]]], "arg1 lower and upper length must be equal"];
	MyCheck[Length[arg2[[1]]], Length[arg2[[2]]], "arg2 lower and upper length must be equal"];
	MyCheck[Length[arg1[[1]]], Length[arg2[[1]]], "arg1 length != arg2 length, must be equal"];
	result = {};
	For[lu=1, lu<=2, ++lu,
		mas = {};
		For[i=1, i<=Length[arg1[[lu]]], ++i,
		    po1 = arg1[[lu]][[i]];
			xq = po1[[1]];
			If[xq != arg2[[lu]][[i]][[1]], Throw["arg1.x != arg2.x"]];
			k = func[arg1[[lu]][[i]][[2]], arg2[[lu]][[i]][[2]]];
			m = func[arg1[[lu]][[i]][[3]], arg2[[lu]][[i]][[3]]];
			AppendTo[mas, {xq, k, m}];
		];
		AppendTo[result, mas];
	];
	result
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
	MyCheck[Length[f[[1]]], Length[g[[1]]], "@MyMult, Length[f[[1]]] != Length[g[[1]]]"];
	n = Length[f[[1]]];
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
	       Print["11"];
	       AppendTo[resLower, MyCombine[fil, gil]];
	       AppendTo[resUpper, MyCombine[fiu, giu]];
	   ];
	    If[fUpperMax <= 0 && 0 <= gLowerMin,
	       Print["21"];
	       AppendTo[resLower, MyCombine[fil, giu]];
	       AppendTo[resUpper, MyCombine[fiu, gil]];
	   ];
	    If[(fUpperMax > 0 && 0 > fLowerMin) && 0 <= gLowerMin,
	       Print["31"];
	       AppendTo[resLower, MyCombine[fil, giu]];
	       AppendTo[resUpper, MyCombine[fiu, giu]];
	   ];
	   
	   If[0 <= fLowerMin && 0 >= gUpperMax,
	       Print["12"];
	       AppendTo[resLower, MyCombine[fiu, gil]];
	       AppendTo[resUpper, MyCombine[fil, giu]];
	   ];
	   If[fUpperMax <= 0 && 0 >= gUpperMax,
	       Print["22"];
	       AppendTo[resLower, MyCombine[fiu, giu]];
	       AppendTo[resUpper, MyCombine[fil, gil]];
	   ];
	   If[(fUpperMax > 0 && 0 > fLowerMin) && 0 >= gUpperMax,
	       Print["32"];
	       AppendTo[resLower, MyCombine[fiu, gil]];
	       AppendTo[resUpper, MyCombine[fil, gil]];
	   ];
	   
	   If[0 <= fLowerMin && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["13"];
	       AppendTo[resLower, MyCombine[fiu, gil]];
	       AppendTo[resUpper, MyCombine[fiu, giu]];
	   ];
	    If[fUpperMax <= 0 && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["23"];
	       AppendTo[resLower, MyCombine[fil, giu]];
	       AppendTo[resUpper, MyCombine[fil, gil]];
	   ];
	   If[(fUpperMax > 0 && 0 > fLowerMin) && (gUpperMax > 0 && 0 > gLowerMin),
	       Print["33"];
	       AppendTo[resLower, MyCombine2[fil, fiu, gil, giu]];
	       AppendTo[resUpper, MyCombine2[fil, fiu, gil, giu]];
	   ];
	];
	AppendTo[resLower, {Last[f[[1]]][[1]], 0, 0}];
	AppendTo[resUpper, {Last[f[[1]]][[1]], 0, 0}];
	{ resLower, resUpper }
];

MyCombine2 := Function[{fl, fu, gl, gu}, 
	{fl[[1]], 1, -fl[[1]]}
];

MyCombine := Function[{f, g}, 
	{f[[1]], 1, -f[[1]]}
];

points1 = {-Pi, 0, Pi, 2*Pi};
ozn1 = {1, -1, 1};
func1 := Function[x, -Sin[x]];
func1d := Function[x, -Cos[x]];

points2 = {-Pi, 0, 2*Pi};
ozn2 = {1,-1};
func2 := Function[x, x*x*x];
func2d := Function[x, 3*x*x];

res1 = Func[points1, ozn1, func1, func1d];
res2 = Func[points2, ozn2, Function[x, x*x*x], Function[x, 3*x*x]];
(*
MyPrint["res1", res1]
MyPrint["res2", res2]
*)
res = Func2[res1, res2];
(*
MyPrint["res1 now", res[[1]]];
MyPrint["res2 now", res[[2]]];
*)

plusFunc := Function[{a,b}, a+b];
minusFunc := Function[{a,b}, a-b];
multFunc := Function[{a,b}, a*b];

myPlusFunc[x_, y_] := MyBiFunc[x, y, plusFunc];
myMinusFunc[x_, y_] := MyBiFunc[x, y, minusFunc];

mult = MyMultFunc[res[[1]], res[[2]]];
MyVisualize[mult, Function[x, multFunc[func1[x], func2[x]]], -Pi, 2*Pi]

plus = myPlusFunc[res[[1]], res[[2]]];
MyVisualize[plus, Function[x, plusFunc[func1[x], func2[x]]], -Pi, 2*Pi]

minus = myMinusFunc[res[[1]], res[[2]]];
MyVisualize[minus, Function[x, minusFunc[func1[x], func2[x]]], -Pi, 2*Pi]

MyVisualize[res[[1]], func1, -Pi, 2*Pi]
MyVisualize[res[[2]], func2, -Pi, 2*Pi]

MakeDemo[points1, ozn1, func1, func1d]
MakeDemo[points2, ozn2, func2, func2d]

Plot[(x*x*x/20 - 5*Sin[x]), {x, -Pi, 2*Pi}]
(*
Plot[(x^2 - 8*x +7)*Sin[2*x-1] + Cos[5*x+1]*2^(2x-3), {x, -2,5}]
*)













