import type { Principal } from '@dfinity/principal';
import type { ActorMethod } from '@dfinity/agent';
import type { IDL } from '@dfinity/candid';

export type Time = bigint;
export interface _SERVICE {
  'getHomePage' : ActorMethod<[], string>,
  'getICPPrice' : ActorMethod<[], number>,
  'getLastPriceUpdate' : ActorMethod<[], Time>,
  'getProduct' : ActorMethod<[string], string>,
  'getProducts' : ActorMethod<[], Array<string>>,
  'status' : ActorMethod<[], string>,
  'updateICPPrice' : ActorMethod<[number], undefined>,
  'whoami' : ActorMethod<[], string>,
}
export declare const idlFactory: IDL.InterfaceFactory;
export declare const init: (args: { IDL: typeof IDL }) => IDL.Type[];
