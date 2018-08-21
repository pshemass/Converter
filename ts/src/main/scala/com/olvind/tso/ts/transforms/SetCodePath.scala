package com.olvind.tso.ts.transforms

import com.olvind.tso.ts._

object SetCodePath extends TreeVisitor[CodePath.HasPath] {
  override def enterTsDecl(cp: CodePath.HasPath)(x: TsDecl): TsDecl =
    x match {
      case xx: HasCodePath => xx.withCodePath(cp).asInstanceOf[TsDecl]
      case other => other
    }

  override def enterTsContainer(cp: CodePath.HasPath)(x: TsContainer): TsContainer =
    x match {
      case xx: HasCodePath => xx.withCodePath(cp).asInstanceOf[TsContainer]
      case other => other
    }

  override def enterTsParsedFile(t: CodePath.HasPath)(x: TsParsedFile): TsParsedFile =
    x.withCodePath(t).asInstanceOf[TsParsedFile]

  override def withTree(t: CodePath.HasPath, tree: TsTree): CodePath.HasPath = t / tree
}
