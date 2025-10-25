export const idlFactory = ({ IDL }) => {
  const Time = IDL.Int;
  return IDL.Service({
    'getHomePage' : IDL.Func([], [IDL.Text], ['query']),
    'getICPPrice' : IDL.Func([], [IDL.Float64], ['query']),
    'getLastPriceUpdate' : IDL.Func([], [Time], ['query']),
    'getProduct' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'getProducts' : IDL.Func([], [IDL.Vec(IDL.Text)], ['query']),
    'status' : IDL.Func([], [IDL.Text], ['query']),
    'updateICPPrice' : IDL.Func([IDL.Float64], [], []),
    'whoami' : IDL.Func([], [IDL.Text], ['query']),
  });
};
export const init = ({ IDL }) => { return []; };
