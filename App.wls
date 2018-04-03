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
	Module[{i}, 
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
	];
	AppendTo[lower, {Last[sp], lower[[Length[lower]]][[2]], lower[[Length[lower]]][[3]]}];
	AppendTo[upper, {Last[sp], upper[[Length[upper]]][[2]], upper[[Length[upper]]][[3]]}];
	{lower, upper}
];

MyVisualize := Function[{res, y, le, ri}, 

	lowerNodes = res[[1]];
	upperNodes = res[[2]];
	MyCheck[Length[lowerNodes], Length[upperNodes], "MyVisualize: Length[lowerNodes] != Length[upperNodes]"];
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

MyCombine2 := Function[{fl, fu, gl, gu}, 
	{fl[[1]], 1, -fl[[1]]}
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

res1 = Func[points1, ozn1, func1, func1d];
res2 = Func[points2, ozn2, func2, func2d];

plusFunc := Function[{a,b}, a+b];
minusFunc := Function[{a,b}, a-b];
multFunc := Function[{a,b}, a*b];

myPlusFunc[x_, y_] := MyBiFunc[x, y, plusFunc];
myMinusFunc[x_, y_] := MyBiFunc[x, y, minusFunc];

mult = MyMultFunc[res1, res2];
MyVisualize[mult, Function[x, multFunc[func1[x], func2[x]]], -Pi, 2*Pi]
(*
plus = myPlusFunc[res[[1]], res[[2]]];
MyVisualize[plus, Function[x, plusFunc[func1[x], func2[x]]], -Pi, 2*Pi]

minus = myMinusFunc[res[[1]], res[[2]]];
MyVisualize[minus, Function[x, minusFunc[func1[x], func2[x]]], -Pi, 2*Pi]
*)
MyVisualize[res[[1]], func1, -Pi, 2*Pi]
MyVisualize[res[[2]], func2, -Pi, 2*Pi]

MakeDemo[points1, ozn1, func1, func1d]
MakeDemo[points2, ozn2, func2, func2d]

Plot[(x*x*x/20 - 5*Sin[x]), {x, -Pi, 2*Pi}]
(*
Plot[(x^2 - 8*x +7)*Sin[2*x-1] + Cos[5*x+1]*2^(2x-3), {x, -2,5}]
*)


















(* ::InheritFromParent:: *)
(*Graphics[{{{}, {}, Annotation[{RGBColor[0.368417, 0.506779, 0.709798], AbsoluteThickness[1.6], Opacity[1.], Line[CompressedData["*)
(*1:eJwVl3c8V98fx5GsrM+9xsdIInxSSSISnbetUEZp0JBIKr4aWnZRSVZpaSKk*)
(*EClSuSfJaEgKUSGbJOuzzN/9/XUfz8e9933Oeb9f79c5Z+GeQFcfIQEBgV5B*)
(*AYH/P723HDGdXCiOFd55JcvV8ihH/vUyGVVxLKdGFO+M5FGrUsssFymIY4cE*)
(*ObMCQx4l1ibu4CQhjvsNRtoDU7lUnu89j7ujYrham/XHfT+H4h37FGL1Vgx3*)
(*9zIf9oiMU/FXdKiLe8Vw76X4oreXh6gXX35YqWaJ4ivK/zKympooVZb76E5J*)
(*Eewu43TZMHcAaQTPLWyOE8bKiw2Sqj9Oo/3fS4r4McL47dHCyRfiM+iJ6cFi*)
(*pUhhXFn4y/qS7QxCs/Wvth0TxnmaWbLN1AzyPH+vqnmHMBawG24VKphFKTfX*)
(*/GrWE8bPzJWE31UKgBgVJNZSNwdf1dQ6sOuaEAyJtu5skZuDf5SVm20TEYHM*)
(*Aq2+U9JzsHWCS80fDRHY4XEoSEVsDjYpTTXdj0TgY97UGc9JIXxi5/OURSdE*)
(*4OFmlYetv4Xw/Zq3Jal9IrA3fetYR54QLvplsizinSi0rP167o+9EK4+NyNU*)
(*HSAOyX3KjDhLIbw2aXrm9nlxWJe85+ZSMyFcP4Lsd6SJQ0n3yONDy4XwVZnS*)
(*5GsN4pASJ1v/T14I57d6veSbSoBzi5PqeIcglrkrrOkvOA+qjlU/mQoRxBkM*)
(*K5XHIZLgObZJWeK4IC53vlwvnSwJo//9PqMYJIgdSgQueWZJgtrBCXcDH0F8*)
(*9+WJ/pIvkhC8Z+n0PidBHDzCdT+qJQXaGxPXfZ0viOdVLWscqpaCaNbWjodl*)
(*Ajh6H0v65aw0KGd2ORSXCOABKXZyACEDTzSDnlUUCuC3nHPqkloy8FMt9nxb*)
(*pgCWmgxcPXe9DBjKv9KTTxDA+W+zbw0ly0CXoPrpiF0CuLLjgIyjrCxY/egl*)
(*twgI4NoamemLKbKQDAyx92Oz1L07fYWL78jCnfeufd09s9RaXwux0kxZKG5t*)
(*yJ7/aZZyU8NRhcWy0Cfyc/GlG7PU8S1lLkLNsuCwpW/JQQP6+8HTdspKDNj6*)
(*e7Hkea1ZSvXBLsd2dQbsPXBgMJ05S/kMnXxxncWAkPChxz9mZqjmc4lUuzED*)
(*8rLGlzl8mKE2WRVqftrMAFmegL7u3hnKsVnqoVwCA1SjLGVtt8xQtZ/55bMp*)
(*DGBJnh32Wj9Dufg/lP91iwEW6qIFN/RnqPu1cdsDchhw2F7KQHx6mlph94z5*)
(*p4IB364rGfalTFOOigdXOnAZcGO1gUlm1RQVdN13JMWFgAqpX3YzpVOU3TfF*)
(*44/cCRj6fW6Le94U5bbj439PPQiwjP0VLJIyRbUGdby46UPAYMv5Z757pihF*)
(*767jlScIgJA2A53pSWqtQaBP0i0C/J1jLcOHJ6kTLx1CPt0jIGWRkWtT5yQ1*)
(*+lF/ZDKDgIGPsUHn3k9SC/rShpfnEnBl/qonvdcmqYYPL1njrwjoK4tblm0w*)
(*SRWJVDwabCaAuGxsLqA9SVnebVq24RcB5vs6HLcqTVJ7lufN3m8nIFnG5KCY*)
(*wCSVrKaRqN5LgNnuzhy/TxPUid/rNp0cI8DXML4U4wkq86y39SEOAUliq98z*)
(*iyaoP+oiEW58AnqexPdX35ygSN0d3zkzBCQKmrIW+01QPDM74RviJJQ2dBlH*)
(*ekxQXZ2+dgskSeh+mGDXvGGCWjO3+us1aRJMXbt9LxhNUCtXUVW7SBK60hIf*)
(*9AtNULWs9coOqiRIH1/zzILDp6KaRB94q5Gw2qGn4kY/nwKVpycC1EmIH1vT*)
(*ta6OT5kPm01uX0SCiXWvRs5tPhX79HVQwhIS1LHK2I8kPrUgfa+I2jISRM2c*)
(*30rF8KlNycdy7+mR0LjyhXdQAJ8S0xhfGbWChLL8vyvT9/Ap98FZ8rcBCQ+W*)
(*aAg3uPMpOYlCUUNDEo5qXswwQXyqqiJmyfNVJHjepY76r+RTSexgz25jEqxU*)
(*xq1v6fCpC/ez08RWk0CQO7pnZfiUbeXj/5asIWEiPunZCmE+tX5tCF/XjIQO*)
(*icpobx6Pmlx+7oaaOQkFQsu1q9p5VHxh7MKOtSRcD/Pm8L7xqL7cfIlCRELE*)
(*xLVK3RoeRXH9pIKBBL/gj1c9X/OoA1c9dPUsSHAeFdgXX8Cjtoca7Gmh2STA*)
(*yBg/4FH8uDvPTlmSsGBgv+joDR51JdldS8aKBBHfO02a8TzKzF6x8BrNQ7/r*)
(*szZH8ajUTemectZ0fnaInjgXzKMa1r/Viqb5dfMa+xf+PGprjo30AM0Zm/9j*)
(*/tnJowpbZxSsbEiI+5LRp+rGo7xQDSTSfNSpuWSDHR1PJjruC80eNVIXItbw*)
(*KLUw5qSILQmWNpbbCpfzqKGMwEv6NOu+CV7cpcmjFl4Isd5AM2H+iC/P5FGX*)
(*lY0X7qaZX9JWYyfJox6vvL3Il+Z2Q7mbJwV4lPyNwo3/f1/9xN7/0TiXCuBH*)
(*pP///ydLQ01/9XGp2UHFhStovpZdICHzi0vtZ8fViNIctqinBb5wKffQydv1*)
(*9Px87yk/OvyOSzG/n72bRPMG1Y2nM15wqW0Wzp+taTa6fsahMZdLFf+M1Ruk*)
(*168qV6Iilsal9j7bV3aOZuHEwT+rr3KpqtcLIhRpHpy38NWBWC4l83nkaCqd*)
(*36/nNsfdDuNSNdkad0ia08LLlgruo8db8SW5na5X7OTolIEHlyprYe82ojno*)
(*uM6nvRvp/y2r/MLoekNg4qFqYy61TnZW9y+tD9afCvOJJVxK6vKaPoJmmX18*)
(*qaXqXEpH4kvXUlpPrTv35CWIcanwqrH75rT+Kluuhr2Z4lAypjE+hqYk5Lp/*)
(*2DA2zKFCeacDFtB6Ddlg+M+9mUOVuf10r6f1rbJWRF8th0NdIyrzHtH90WHv*)
(*LeVym0Mtm79+UkufhBw3PHAmkUO1L++NS6b7yXT/qQf9wRzKUd73vCXdf9uv*)
(*DCk/s+JQh6xlFs5okqBx14HXu4pDBVq9bJTQIGHgYXaDsi6Hsiqy6RKn+/kU*)
(*tScxQpZDmQyPyLapkHBzoHGu4y821Vy2gddNkLCHvbIzrI5NGVV6FkXL0noR*)
(*SMIFb9lUUiDxS5H2k1J5h9OKOWyK3xAUTtL+0wLUcEcwm+opyHX3o/1J+VrW*)
(*j5OybCpau6Q1toeAjvvCLx7PYVMpmicp104Cch57XW3jjFO/J4+aSdH+aFqu*)
(*4mLza5zSC2Eyt9N+uv1vQqVszjiVGpld6PaBgJtWJwqyrMap09fvHKl5TMCe*)
(*DQ3xLavGKTlmU+6rbAJ0txkclNIdpwY0379Mo/27NGBQ+4jsOHXucL2XHe33*)
(*LTd231r7a4zqDy6HsVgClIbtzzUEj1HiE67Pv9P7xZ205Vod/mNUY4/n4g1e*)
(*BCzarPB2aOcYVdznsrXEkwC90s4ZUbsxamnac66/G73fnA0LNlUco5aLvr29*)
(*Duj9hFnkc7d4lOpryMv4p0iPDwus9/NGqJgNpqW8MgagsbkdxwZHqG2B115L*)
(*vmBAxYPB8Kj2Eao0PoMt/5QBtRKlL1OrRyhzjrecWBYDOr5tWll7fYTaH1cR*)
(*eSCeAfP2x2qsXD1C3at89t3AgwGeSWyB6ZPDlG5MV2jHoCzMXrKptQkYpuSe*)
(*ycq/75aFtNiU1Pg9w1RGJNmY1SoL/WeMVqk7DFPt52t7bepkITj42AEr1WGq*)
(*cujce8mnsrCgeOARGfqP2t595F5NsCzEOAxsXiYyRIXqLXeyHJKBTUf7H+5m*)
(*/qG8qtm3q0qkwONiy87xmQHK585Dl10ZUrAn7QN5vnuAEnOr+N2RIAVBdbmh*)
(*+YUDVJ/tiS+5vlIQv/Swy4zjAOX7PfjCJlIKqrsmeKmR/ZRJ1Ioto36SYL5Z*)
(*0r5poJdyPp1yv3nuPNBetbzH6XUXNb4jkPgkLgZzcpNqV935RXVf9YvZ80UQ*)
(*tGvd+K3ldVTEixfHagvHUYdRhFFL/BPq6IJDhzPzvqGL3/Pd3sWUoZAnhcd5*)
(*op2Uq9JDaUnfb0jkjEMRV4JPlaBdC9xY7cjG5kbxRsc5uMIxYLW2ZTsyP5h2*)
(*eL3fHPx5W6gb36MdPf2v7Znx2Tm450jqubsJ7SjP7EJM/8s5mMxu+veH046K*)
(*w0OWZesK4wBZZ3z23W+kpF91JWLuXLzoN3g939OJoiPlchWfiOCkKI10pds9*)
(*yN1M7P3Hb+J49+TOnUef96Bu8YzJvYPiWP9oqtLnzz1I6tqGsj9zJHCtj1zi*)
(*WaFe5FP0/lmFgQSWWCcS+m9fL9KoCM1OS5DAUTID7u8M+tBGNZXrArbz8OFb*)
(*BRJB1f1on2P2o8YMSWwhP/Tuw+9+dEEr0F6jVBLLxutGak/2I2vnY1HbP0vi*)
(*vPB07o9lA6jAPic5kS+JB/akdNleHkBfuZrRDCcpvGfxyTLVnX+Q6vPXnduG*)
(*pbDrczhcPTaI5CcsQu/pyOCstv2PA+f9RTFRVzU1VsvgKbHLPQqaf9FFpsXu*)
(*pPUyONOje7uP619ESEkFmRySwXyBWCuhgr/ItvLNm6wCGXzP4aucWcAQGug8*)
(*0hWhIouH2n2K8/v+oSrOgW25N2SxRnTViSUCw0jJ7vVu8/uy+KPST/nZecMo*)
(*f6lZ/NtsWaxuNdc5U3MYLScO2dPnXfw+ZUvFuOswuqu+8ymzQRarrZl6nFQw*)
(*jPqdTSznSjPwu2ibsA8BI+i09Gdb8xMMnHhxhfSd0yPIsVFri2w4A29Pmn/n*)
(*v/MjqLJ7I6spmoGHbrFfy6eNoNqNNVx0mYEVih5M72oYQRHTt4pDcxnYt2Nu*)
(*6PiaURRWv9voQRsD6/eNSFbZjyK29rqOgm4Gnvj769aNzaOojpCcKfjDwPH8*)
(*Z6/WBo6i0G3ZAXFcBn4u6zt1Pm0UueH43IfSBBZBlafni48hye1R0ZGmBK6z*)
(*Lpw3LD+GboaH9ZYjAt9cfye1XGMMbUn4dYhrTWA99+CX+8zG0LDRisbVGwns*)
(*fkh7siBwDOnFNaZqehM4MzXmlG3jGIpSy1l37jyB/7t/WEKpcwwhydhRXhyB*)
(*TbN23vzzbwzlF86G7EgicG3hqtJE8XHUVSGmM3uDwJyaHn6L2TjSj63zzsgh*)
(*sC3P9mRg+jjaMTa05kgNgd3cH9r75Y+j5g2/jb59JPDuonlMr5fjKMqJH7e4*)
(*jsCn/qt77vp1HJVbH7craCRwXt+2caM5bJTzVOy+YSeBX9q+eqsnw0YlVz2M*)
(*TXoIXJ2hdllHhY227lkir99P4I7dnSuUVrKRp3/7rakhAss3Hwyc2sNG3EiH*)
(*FLEJAmsYf17LDmCjR6Ei6nlTdD5SVkgPnWKj4kGlVrtZAtu7cB63JbNRSN6j*)
(*gS3CJA6pCR8oL2ej40Wn5wxIkfiCTseLl7VsdCS+rkdflsRXo60vFLWw0ZW/*)
(*bQMHCBI/sRBnZY6ykVC2m2OZAom7XiT7XNDgoOnxQeNvaiQeUWQbRulxkBnb*)
(*vuulOolnjm0RPm3KQbvU1t+7pkFipoFq+kFXDtqrr42WaJNYKzHssM9ODhrO*)
(*nKvWqUNig6F2i53+9Hvvy8IJi0nsmPOgfWMUB10uFPldvpTE28TEntjHc1Cb*)
(*qcbX9Xok9vX1D7e4yUHv2FveVS4ncYTGcrWVhRzkPfEm7aYBiZ/eLPFQ6OAg*)
(*9vdSmUJjEmOe8hKZIQ7qtcqqazYh8Uf30AnRCQ768zfvDHs1iXsIyxt8Bhep*)
(*+ca8EDMj8dh/GX6j87kIhtVXC5iTWOCziMmfxbT0DQ5m/KFZ+eKHxp8WXOTw*)
(*esj4DiKxTv+yzAYnLmqL0tjhAyQ2tEs8VruNizSnZvepW5DY4sGodZUPF93h*)
(*yW35TPOGOZvlcBAX1UaxWYctSezhVdxZEspFd9vX/BSzIrEfpfS04AIXcS+f*)
(*D7xM87H5IVE5KVy0xCPqN8OaxFGnW13S73NRyIpG/bM0ByU0jezK5SLBL2dO*)
(*9NG8O70uSfUFFw0W9JEWNvR4xTUrmiu4qOdno2A8zeYfyr+k1HGR2wJti880*)
(*L217GeT6k4sK5xXVCdvS6xsrYsj0cRGb752vR7O4aF7BhzEuahdd8NORZp5y*)
(*lsv5WS7aqvRt8y6ae/XujVjP46Gn34+r0Odj3Gh5I0lQkYeki4WXetFc4Z68*)
(*okyDh5IGT8U40/zU/+KXU3o89PJE+2JDmtPCzgYZm/JQjoaFvCTNicmhjHEb*)
(*Htp8Ms/uOz2/8MzggicuPPT1K8LXaQ4oDXQ5tIOHrroQYU4076j1G1m8n4dS*)
(*W+wjOPT6HTu8knqO8lD+AsnKyzSbcravSI/gocs3Yzdo0bxYYtOXXXE8ZBjP*)
(*Yz6i88tUcwpSvc5D30QSF2nRLGJgy2hO5yGTlsT/LtP16dpm4uL6kocefx+o*)
(*dKTr9/XQihHpKh4Svtf++Rpd7/JI3aQP9Tz0d+KEQhOth7sPVb9YD9Dzc5XY*)
(*brCWxPGv5YMEOTx01qx28wZaPyFfpBllgnwUceV93E5ab9v5Ai7GSnw0q2yZ*)
(*42FK4nVSE8Nji/io/sLzBHtanyYLxxKf6PORZ+/mvMW0fuXXddcttuej0DS7*)
(*lGojEgvvaP2vx42PVl554xljSOLR/5pk03fxUd2tZx4mK0lcd73GWfU4H/l/*)
(*ujlzQp/E1OPy4e9RfBT88/d9Ubp/8vDLxJR4PtpRt+F07DISX+zLrZPO5CPH*)
(*rujPB3VJbGuS7CzYwEfOrvO56ZokNnK8OPy6nY+Mry6w+baQxIt2n008NchH*)
(*d42eVfEWkFjwfHDd2JwJJGKA/NVUSfyqabtzj8EEMsxO1vxJ0v1+XNP5Q+IE*)
(*utzh+t9CQRLD1FhHzO0JBB9sNXbMEHhDZMUxy5wJZPROfjx2ksAHLvqkvnhL*)
(*8/3HvFdsAmfczerJ4kwgvU7XpFO0PypWLwk96zmJPl5yPe5ZS2BtpykZ2D+J*)
(*bqbe9yp9T2DD+o9pk8cm0WtT0WMSVQR2/nmo+nDCJGKFnVaOpAgcO5xPepVP*)
(*osMXL5yszifwFNMwx5w1hVZtsQ7AlwgscUfYnG84haSR1W6LCwRW0mz4XGQx*)
(*hQT8z8YVnSXwqmXB7CUeU6iweumNoNMEDrQoAeX4KZRf4Zdj60f7936zJs7Y*)
(*FIo/3SOwk96/qkqthZ9Q08j+x3ipUycDM69fbEz6MI2EnXXzz/xiYP9j9dlH*)
(*mqZR3jHTptwmBpbU3+1k/G8aHf0W8L71AwO7Pjh9tUxtBrWnih95+pSBfyYU*)
(*sj6FzqD3VaoJiyMZeHivutPAmllUF8Iw2EoysIXlvgUf7WbRu+tvC5mSDJy8*)
(*IG8k120WNTMC8j4JM7BRy5qrQQdmUW+f1EJ5jiw+tXFLG+/mLKpu2W2u/F0W*)
(*C5vGB4lOzCInIfbTWfo8Eic6s57wFAAFqrbGQV4WV+hdyo3aIwAtST7pPfNk*)
(*8fRmVdkxPwHYp2nmfVRIFgdkmDZ8PSYABtGfHb+0ymBni+O7UhIEID5dx3t/*)
(*kgwmTw8fYb4VgCsFuZtej0jjm0Mdt9R0BSE2w87M8K4UzmqoGtLlCYL0zXPn*)
(*4/IlcN2WtNWhM4LQWHI0h31VAk80h5z9LCwEHScrJu3CJLBj6wrlowwh0K0I*)
(*25btKIFHe1JtXusKgZ9a1tptfeLYjBd4a+MOIdBs8nwTrSKO65WZ64+WC0FM*)
(*TN9dzyBRPLXL78HruDnAPp7ZXPZrDs4f/bqeKTIXHN86ygXW8KjND5c5uNwV*)
(*Abnvr4N++LdQW/1cTV3ni8Gg9bNPi870opROJ7NPl8WhzriDKLo6g95FGd/Q*)
(*1p0HE9E/X4s+Fwc33cEuuweS0Hgm0NYmnwFf2fEX7UAa/pGeW1hvFKCgZkpd*)
(*BsvAqNmLDM3VKtD1+N5e1i1ZePx3yn+99QLoFzH1NfFjwEj/PE/NGwthfrfJ*)
(*rsfaBLQ21s8yV2hC66v6P92LCUj22q1/wlgT7lw5eGLBMgKwYmVZvbkmqFvf*)
(*S0peSQCn49voyfWaoJEuWnGCvrelbZr7MNFbE1hejSyb7QTUn4nvbL2iCYa/*)
(*joz+ukRA0u5OlakxTVjfkBctwyYgYcD2xcqMRXBme4CIKY+Anrup65wfLoJX*)
(*bcvO7Z0k4HG58S6/vEWgN/D4/AtBEs5pq6heLFkE5GzORW/63ry3Z83AzY+L*)
(*4CcrK6lYhwT3CD3XrrFFEBhy9/au7SS8n1c6rIG0IFtwl1rsDhIEdyluKbDS*)
(*go4YtbtFu0ng3G/eusZeCzYl3b4nvo+EK+vdDSxdtMAkMzX96RESaowvblXx*)
(*1gKhumsPRS+R0K5/attUtBZc0Ux8lk+RoCbiYxRZrQWta5dVppeTsFR33Vr4*)
(*qAWs7e8br70jYa2EdcrkZy14lSjMC/9AwlUPdYN9TVrQOX3C1LmJhLiSaHui*)
(*RwsMvntRw0MknF0jPVEipA11cStrVqjJgf/hpddcVmuDcnZds9ZCOfjp2t90*)
(*y0wb9r49NKC0SA5+qa1L6kLawONnzRPSlQOvYsGj+221YcE+1Q31RnLQZee7*)
(*aJ2bNgRYzP162EkOsg7mJnsd1AYpTuPPpyFy8Oyomo3QLW2483dxTm+4HBw6*)
(*t+7Q/LvaoN8dclzlDD3e+Nc8wzRt2PRNgzhzQQ5i5p4/tyVbG24VBti5XpWD*)
(*sBVV9QFF2qAbKFI4nC8Hzrefcbs/aINtn9G5ZZ1yUPHVMViMpw3f285v8uqR*)
(*A9vRZY8eTWiDf9OPhSn9ciAQGePoMK0NlyojX039kwOib7wtXEgHvmV8Gvkw*)
(*JQcRQf9ulEnqgLeXj6e/gjxo77DqvaKuAxEtV1ZkrZMH/a8FIqU2OqDh9y4+*)
(*w1EenqQvFvlgpwPv2Ow/9zfKg8h5D7PmdTogwdiSeWuzPNi9Stjy10kHrtgp*)
(*qSR7yYP7JeMr45t1ILvotnDoSXloPv0qJtJHB+riM5tcH8qDSp9+Q3aUDhxW*)
(*bTJ0fiwPjod+Cvqe1QH5HNFkp3x5KDKUo9RjdGD7Oz9H+2fyMOfHXG78BR3o*)
(*mtR9Y/5GHm4SZ55sSNQBnl9+DqtZHl4naAztuK0DC61KQmfEFOC7xrjel2c6*)
(*YBYm/XlwngKA90ZRVKwD7i/2qv+QVoCFEW29OSU6ELec8bZYTgGi3/76FPKS*)
(*jqfiLxakrgAxkWFnxN/Q8x1XudxprACh15Y71Xyg85MZnl3tqwAP/i4P57Xp*)
(*QOrvBv7z/QoQvqI7aNlvHXiuutThwUEFuPCNvnF16MBg8ve/kYcVILY9eaC8*)
(*Swe2hq9YuSZMAfSDJL8e7dcB/a2dr3NTFCCnKD4oeVQH2sTtviZXKECvmkfa*)
(*xzkssFzo9htXKYDW91sug8IsyDDZ9W/ovQJ8NFHLlhBhgf++4/McviiA77Wv*)
(*nlZiLOBUZFnNaVWA5HXLvB9IskAqQqzoCFcB8MnvP5bLs2ANp+bKJl1F0FQQ*)
(*zA3TYsG1Dkd3hURFuFBRTM2zZoFE4qpxqcuKsLCwfKyf5jBz9eS5VxXBuPuP*)
(*ULUNC3yvj30aT1WENMPC8jN2LDDccNPma5YivMyYNplYz4K6F72GiZQiNGQ/*)
(*utjjQsdLiiIl/ilCkTO1rXkXHW/twQLBUUUwW6VTX7qbBSN/Nm/kjyvCjXen*)
(*sm95seC77eKLfROK8C05PNzLmwVZU5+FqkSYMLrvgMOwLwus/eaPnlFjwgan*)
(*PwLaAXQ8VFI37cSEdacmzGRCWXDhU3KlkwsT3mokai4MY8EVj0Ovbm9iwjZV*)
(*Ix3DcBbkHNfINvdgQoyCw+0dkSxozI8LD/Vjwv6NywbKolmgp+6lNx3FhPU2*)
(*LUY4ngWtAhJxU8VMsLPctKr3Pgv647siHV8y4cLt6bWsdBaMq1LHb5Ux4dQb*)
(*Lc0DGfR6Vx/1NnvHhGMaAymcTBasCmo1DalnwsQSjU+LHrMg/ndh/+QgE1Kf*)
(*J5R3PmfBjcBLbQ7DTHDvmONmU0LXd3pfQ+oYE4KWCy14+IIFpUrz36yZYMLK*)
(*xJzI469Y0ONy7vppUSXI1yOV9cvp/LV5xX+QUALro2h5xlsWTB0yO6sirQRS*)
(*G+WfKL9jARE7EvhSTgmSNeskZapZMJ/50WceUwlCzpSNx9WwgJWZ6eGhogSL*)
(*zffvlvrAAgPDSJdHakpQwGrYnPiRBeblHnaTC5VgWEv0h3wtC/4HwmJ7NQ==*)
(**)
(*"]]}, "Charting`Private`Tag$13125#1"]}, {}, {}}, AspectRatio -> GoldenRatio^(-1), Axes -> {True, True}, AxesLabel -> {None, None}, AxesOrigin -> {0, 0}, DisplayFunction -> Identity, Frame -> {{False, False}, {False, False}}, FrameLabel -> {{None, None}, {None, None}}, FrameTicks -> {{Automatic, Charting`ScaledFrameTicks[{Identity, Identity}]}, {Automatic, Charting`ScaledFrameTicks[{Identity, Identity}]}}, GridLines -> {None, None}, GridLinesStyle -> Directive[GrayLevel[0.5, 0.4]], ImagePadding -> All, ImageSize -> {614.6875, Automatic}, Method -> {"DefaultBoundaryStyle" -> Automatic, "DefaultMeshStyle" -> AbsolutePointSize[6], "ScalingFunctions" -> None, "CoordinatesToolOptions" -> {"DisplayFunction" -> ({(Identity[#]& )[Part[#, 1]], (Identity[#]& )[Part[#, 2]]}& ), "CopiedValueFunction" -> ({(Identity[#]& )[Part[#, 1]], (Identity[#]& )[Part[#, 2]]}& )}}, PlotRange -> {{-Pi, 2 Pi}, {-4.818748883428429, 12.402510494825918`}}, PlotRangeClipping -> True, PlotRangePadding -> {{Scaled[0.02], Scaled[0.02]}, {Scaled[0.05], Scaled[0.05]}}, Ticks -> {Automatic, Automatic}]*)



