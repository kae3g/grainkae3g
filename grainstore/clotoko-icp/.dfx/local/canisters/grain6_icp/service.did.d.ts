import type { Principal } from '@dfinity/principal';
import type { ActorMethod } from '@dfinity/agent';
import type { IDL } from '@dfinity/candid';

export interface _SERVICE {
  'getGrainbook' : ActorMethod<[string], string>,
  'getGraincontacts' : ActorMethod<[string], string>,
  'getGraincourse' : ActorMethod<[string], string>,
  'getGrainpath' : ActorMethod<[string], string>,
  'getGraintime' : ActorMethod<[string], string>,
  'status' : ActorMethod<[], string>,
  'updateGrainbook' : ActorMethod<[string], string>,
  'updateGraincontacts' : ActorMethod<[string], string>,
  'updateGraincourse' : ActorMethod<[string], string>,
  'updateGrainpath' : ActorMethod<[string], string>,
  'updateGraintime' : ActorMethod<[string], string>,
  'whoami' : ActorMethod<[], Principal>,
}
export declare const idlFactory: IDL.InterfaceFactory;
export declare const init: (args: { IDL: typeof IDL }) => IDL.Type[];
